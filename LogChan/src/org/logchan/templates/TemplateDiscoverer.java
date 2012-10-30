package org.logchan.templates;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.logchan.core.SystemConstants;
import org.logchan.util.ConvertUtil;

public class TemplateDiscoverer implements TemplateDiscoverable {

	private String[] defaultDelimitChars = { ",", "\\s", ";" ,":"};
	private List<String[]> escapeSequences;
	private int sampleCount = 10;
	private List<String> samples;

	public TemplateDiscoverer() {
		samples = new ArrayList<String>();
		escapeSequences = new ArrayList<String[]>();
	}
	
	protected List<String> getSamples() {
		return this.samples;
	}
	
	protected List<String[]> getEscapeSequences() {
		return this.escapeSequences;
	}
	
	protected void addEscapeSequence(String[] seqArray) {
		if(seqArray != null)
			escapeSequences.add(seqArray);
		else
			throw new NullPointerException("The String array passed in cannot be null");
	}
	
	protected void readSampleLines(String filename, Long filesize) throws IOException {
		RandomAccessFile raFile = new RandomAccessFile(new File(filename), "r");
		samples.clear();
		for (int i = 0; i < sampleCount;) {
			String line = readRandomLine(raFile, filesize);
			if (line != null) {
				samples.add(line);
				i++;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> discoverTemplate(String filename, Long filesize)
			throws IOException {
		Double[] scores = new Double[defaultDelimitChars.length];
		Map<Double, Map<String, Object>> resultMap = new HashMap<Double, Map<String,Object>>();
		readSampleLines(filename, filesize);
		
		escapeSequences.clear();
		escapeSequences.add(new String[] { "\"", "\"" });
		escapeSequences.add(new String[] { "\\[", "\\]" });

		int j = 0;
		for (String delimiter : defaultDelimitChars) {
			String regexStr = buildRegularExpression(delimiter, escapeSequences);
			Map<String, Collection<?>> parseMap = parseFor(regexStr);
			scores[j] = getCandidateScore(parseMap);
			Map<String, Object> summaryMap = new HashMap<String, Object>();
			summaryMap.put(SystemConstants.DERIVED_REGEX, regexStr);
			summaryMap.put(SystemConstants.IDENTIFIED_COL, (Integer)Collections.max((List<Integer>)parseMap.get("COL_COUNT")));
			summaryMap.put(SystemConstants.COL_DATA_TYPES, ((List<Vector<?>>)parseMap.get("DATA_TYPES")).get(0));
			if(((Vector<Class<?>>)summaryMap.get(SystemConstants.COL_DATA_TYPES)).contains(Date.class)) {
				summaryMap.put(SystemConstants.TIMESTAMP_COL, (Integer)((Vector<Class<?>>)summaryMap.get(SystemConstants.COL_DATA_TYPES)).indexOf(Date.class));
			}
			summaryMap.put(SystemConstants.LOG_DELIMITER, delimiter);
			summaryMap.put(SystemConstants.LOG_TYPE, SystemConstants.UNKNOWN_FORMAT);
			resultMap.put(scores[j++], summaryMap);
		}
		
		Arrays.sort(scores);
		
		return resultMap.get(scores[0]);
	}

	@SuppressWarnings("unchecked")
	protected Double getCandidateScore(Map<String, Collection<?>> parseMap) {
		Double candidateScore = 0.0;
		int stringCount = 0;
		int otherCount = 0;
		if ((parseMap.get("DATA_TYPES") instanceof List<?>)
				&& !((List<?>) parseMap.get("DATA_TYPES")).isEmpty()
				&& (((List<?>) parseMap.get("DATA_TYPES")).get(0) instanceof Vector<?>)) {
			List<Vector<?>> dataVectors = (List<Vector<?>>) parseMap.get("DATA_TYPES");
			for(Vector<?> classVec: dataVectors) {
				for(Object classType: classVec) {
					if(classType instanceof Class<?>) {
						Class<?> castedType = (Class<?>) classType;
						if(castedType == String.class) {
							stringCount++;
						} else {
							otherCount++;
						}
					}
				}
			}
			
			candidateScore = stringCount/(double)otherCount;
		}
		
		return candidateScore;
	}

	protected Map<String, Collection<?>> parseFor(String regexStr) {
		Map<String, Collection<?>> parseData = new HashMap<String, Collection<?>>();
		List<String[]> splitList = new ArrayList<String[]>();
		List<Integer> colCounts = new ArrayList<Integer>();
		List<Vector<?>> dataTypes = new ArrayList<Vector<?>>();

		String[] splitString;
		Vector<Class<?>> typeVector;
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile(regexStr);
		Matcher regexMatcher;
		for (String sample : samples) {
			matchList.clear();
			regexMatcher = regex.matcher(sample);
			while (regexMatcher.find()) {
				boolean added = false;
				for(int i=1; i<= regexMatcher.groupCount(); i++) {
					if(regexMatcher.group(i) != null) {
						matchList.add(regexMatcher.group(i));
						added = true;
						break;
					}
				}
				if(!added) {
					matchList.add(regexMatcher.group());
				}
			}
			splitString = matchList.toArray(new String[0]);
			splitList.add(splitString);
			colCounts.add(splitString.length);
			typeVector = new Vector<Class<?>>();
			for (String col : splitString) {
				typeVector.add(ConvertUtil.getDataType(col));
			}
			dataTypes.add(typeVector);
		}

		parseData.put("PARSE_LIST", splitList);
		parseData.put("COL_COUNT", colCounts);
		parseData.put("DATA_TYPES", dataTypes);

		return parseData;
	}

	protected String buildRegularExpression(String delimitChar,
			List<String[]> escapeSequences) {
		String regex = "[^" + delimitChar;
		List<String> escapeGroups = new ArrayList<String>();
		for (String[] escChars : escapeSequences) {
			if (escChars.length == 2) {
				if (escChars[0].equals(escChars[1])) {
					regex = regex.concat(escChars[0]);
				} else {
					regex = regex.concat(escChars[0] + escChars[1]);
				}
				String escapeGroup = escChars[0] + "([^" + escChars[1] + "]*)"
						+ escChars[1];
				escapeGroups.add(escapeGroup);
			}
		}
		regex = regex.concat("]+");
		for (String escGrp : escapeGroups) {
			regex = regex.concat("|" + escGrp);
		}

		return regex;
	}

	private String readRandomLine(RandomAccessFile raf, Long filesize)
			throws IOException {
		int fileInt = 0;
		if(filesize.longValue() > Integer.MAX_VALUE) {
			fileInt = Integer.MAX_VALUE;
		} else {
			fileInt = (int) filesize.longValue();
		}
		String line = null;
		Random ra = new Random();
		raf.seek(ra.nextInt(fileInt));
		if (raf.readLine() != null)
			line = raf.readLine();
		return line;
	}

	protected void printParseData(Map<String, Collection<?>> parseMap,
			String delimiter) {
		System.out.println("-------------Start Data------------");
		System.out.println("Delimit Char: " + delimiter);
		for (String key : parseMap.keySet()) {
			System.out.println("Key is : " + key);
			if (parseMap.get(key) instanceof List<?>) {
				for (Object obj : (List<?>) parseMap.get(key)) {
					if (obj instanceof String[]) {
						System.out.print("Attributes: ");
						for (String str : (String[]) obj) {
							System.out.print(str + ", ");
						}
						System.out.println();
					} else if (obj instanceof Vector<?>) {
						System.out.print("Data types:");
						for (Object vecObj : (Vector<?>) obj) {
							if (vecObj instanceof Class<?>) {
								System.out.print(((Class<?>) vecObj)
										.getSimpleName() + ", ");
							}
						}
						System.out.println();
					} else {
						System.out.println("Col Count: " + parseMap.get(key));
					}
				}
			}
		}
		System.out.println("-------------End Data-------------");
	}
}
