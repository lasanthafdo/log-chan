package org.logchan.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.logchan.formats.LogFormattable;

public class ApacheLogParser implements LogParseable {

	public static final int NUM_FIELDS = 9;	
	// The pattern goes like match 'digits or signs class 1 or more|IP|','any
	// non-whitespace characters|user?|',
	// 'any non-whitespace|login?|','starts with [,then word chars,: and /
	// class, then whitespace, then + or -, ending with 4 digits and ]|datetime
	// with timezone|',
	// 'any sign,but reluctantly inside quotes|URL|','3 digits |Response
	// Code|','Any no. of digits |#Bytes|',
	// 'Starts with " and has no " in middle and ends with " |Referrer|','Same
	// previous pattern |Client|'
	private String logEntryPattern = "^([\\d.:]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+|\\-) \"([^\"]+)\" \"([^\"]+)\"";
	private Map<String, Object> metaData;
	private int matchMode;

	public ApacheLogParser() {
		metaData = new HashMap<String, Object>();
		matchMode = 0;
	}

	public void setMatchMode(int mode) {
		this.matchMode = mode;
	}

	@Override
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public List<String[]> parseLog(InputStream is) throws IOException {
		List<String[]> messages = null;
		int totCount = 0, matchCount = 0, totalBytes = 0;
		int groupCountMax = 0; 
		int groupCountMin = NUM_FIELDS;

		if (is != null) {
			messages = new ArrayList<String[]>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			Pattern p = Pattern.compile(logEntryPattern);
			Matcher matcher;
			if (matchMode == SystemConstants.MATCH_ENTIRE_REGION) {
				groupCountMax = NUM_FIELDS;
				while ((line = reader.readLine()) != null) {
					totCount++;
					totalBytes += (line.length() + 1); // Add one for CRLF
														// character
					matcher = p.matcher(line);
					if (matcher.matches() && NUM_FIELDS == matcher.groupCount()) {
						matchCount++;
						messages.add(new String[] { matcher.group(1),
								matcher.group(2), matcher.group(3),
								matcher.group(4), matcher.group(5),
								matcher.group(6), matcher.group(7),
								matcher.group(8), matcher.group(9) });
					}
				}
			} else if (matchMode == SystemConstants.MATCH_FROM_START) {
				while ((line = reader.readLine()) != null) {
					totCount++;
					totalBytes += (line.length() + 1); // Add one for CRLF
														// character
					matcher = p.matcher(line);
					if (matcher.lookingAt() && matcher.groupCount() >= 2) {
						matchCount++;
						if(matcher.groupCount() > groupCountMax) {groupCountMax = matcher.groupCount();}
						if(matcher.groupCount() < groupCountMin) {groupCountMin = matcher.groupCount();}
						String[] matchArray = new String[matcher.groupCount()];
						for(int i =0; i < matcher.groupCount(); i++) {
							matchArray[i] = matcher.group(i+1);
						}
						messages.add(matchArray);
					}
				}
			}
		}

		metaData.put(SystemConstants.TOT_LINE_COUNT, new Integer(totCount));
		metaData.put(SystemConstants.TOT_LINE_PARSED, new Integer(matchCount));
		metaData.put(SystemConstants.TOT_BYTES_READ, new Integer(totalBytes));
		metaData.put(SystemConstants.AVG_BYTES_PER_LINE,
				new Double(totalBytes / (double)totCount));
		metaData.put(SystemConstants.MAX_COL, new Integer(groupCountMin));
		metaData.put(SystemConstants.MIN_COL, new Integer(groupCountMax));

		return messages;
	}

	@Override
	public void setLogFormat(LogFormattable format) {
		// TODO Auto-generated method stub
		this.logEntryPattern = format.getRegex();
	}
}
