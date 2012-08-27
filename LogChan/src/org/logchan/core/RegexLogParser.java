package org.logchan.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.logchan.formats.LogFormattable;

public class RegexLogParser implements LogParseable {

	private String logEntryPattern = "";
	private Map<String, Object> metaData;

	public RegexLogParser(LogFormattable format, Map<String, Object> metaMap) {
		this.logEntryPattern = format.getRegex();
		metaData = metaMap;
	}
	
	@Override
	public List<String[]> parseLog(InputStream is) throws IOException {
		List<String[]> messages = null;
		int totCount = 0, matchCount = 0, totalBytes = 0;
		int groupCountMax = 0;
		int groupCountMin = Integer.MAX_VALUE;

		if (is != null) {
			messages = new ArrayList<String[]>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			Pattern regex = Pattern.compile(logEntryPattern);

			System.out.println("Col#: " + metaData.get(SystemConstants.IDENTIFIED_COL));
			Matcher regexMatcher;
			String[] splitString;
			List<String> matchList = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				totCount++;
				totalBytes += line.length();
				regexMatcher = regex.matcher(line);
				while (regexMatcher.find()) {
					if (regexMatcher.group(1) != null) {
						matchList.add(regexMatcher.group(1));
					} else if (regexMatcher.group(2) != null) {
						matchList.add(regexMatcher.group(2));
					} else {
						matchList.add(regexMatcher.group());
					}
				}
				if(groupCountMax < matchList.size())
					groupCountMax = matchList.size();
				if(groupCountMin > matchList.size())
					groupCountMin = matchList.size();
				splitString = matchList.toArray(new String[0]);
				if(splitString.length >= (Integer)metaData.get(SystemConstants.IDENTIFIED_COL)) {
					matchCount++;
					matchList.clear();
					messages.add(splitString);
				}
			}

			metaData.put(SystemConstants.TOT_LINE_COUNT, new Integer(totCount));
			metaData.put(SystemConstants.TOT_LINE_PARSED, new Integer(
					matchCount));
			metaData.put(SystemConstants.TOT_BYTES_READ,
					new Integer(totalBytes));
			metaData.put(SystemConstants.AVG_BYTES_PER_LINE, new Double(
					totalBytes / (double) totCount));
			metaData.put(SystemConstants.MAX_COL, new Integer(groupCountMax));
			metaData.put(SystemConstants.MIN_COL, new Integer(groupCountMin));
		}
		
		return messages;
	}

	@Override
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	@Override
	public void setMatchMode(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogFormat(LogFormattable format) {
		this.logEntryPattern = format.getRegex();
	}

	@Override
	public Vector<Class<?>> deriveColumnTypes(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

}
