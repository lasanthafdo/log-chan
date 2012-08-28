package org.logchan.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
		long start_time = 0;
		long tot_time = 0;

		if (is != null) {
			messages = new ArrayList<String[]>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			Pattern regex = Pattern.compile(logEntryPattern);

			Matcher regexMatcher;
			String[] splitString;
			List<String> matchList = new ArrayList<String>();
			start_time = Calendar.getInstance().getTimeInMillis();
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
					messages.add(splitString);
				}
				matchList.clear();				
			}
			tot_time = Calendar.getInstance().getTimeInMillis() - start_time;
			metaData.put(SystemConstants.PARSE_TIME, (Double)(tot_time/(double)1000));
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
		if(metaData != null && metaData.get(SystemConstants.COL_DATA_TYPES) != null)
			return (Vector<Class<?>>)metaData.get(SystemConstants.COL_DATA_TYPES);
		
		return null;
	}

}
