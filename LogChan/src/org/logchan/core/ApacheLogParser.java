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

public class ApacheLogParser implements LogParseable {

	// The pattern goes like match 'digits or signs class 1 or more|IP|','any non-whitespace characters|user?|',
	// 'any non-whitespace|login?|','starts with [,then word chars,: and / class, then whitespace, then + or -, ending with 4 digits and ]|datetime with timezone|',
	// 'any sign,but reluctantly inside quotes|URL|','3 digits |Response Code|','Any no. of digits |#Bytes|',
	// 'Starts with " and has no " in middle and ends with " |Referrer|','Same previous pattern |Client|' 
	public static final String logEntryPattern = "^([\\d.:]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+|\\-) \"([^\"]+)\" \"([^\"]+)\"";
	public static final int NUM_FIELDS = 9;
	
	private Map<String, String> metaData;
	
	public ApacheLogParser() {
		metaData = new HashMap<String, String>();
	}
	
	@Override
	public Map<String, String> getMetaData() {
		return metaData;
	}

	public List<String[]> parseLog(InputStream is) throws IOException {
		List<String[]> messages = null;
		int totCount = 0, matchCount = 0, totalBytes = 0;

		if (is != null) {
			messages = new ArrayList<String[]>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			Pattern p = Pattern.compile(logEntryPattern);
			Matcher matcher;
			while ((line = reader.readLine()) != null) {
				totCount++;
				totalBytes += (line.length() + 1); // Add one for CRLF character
				matcher = p.matcher(line);
				if (matcher.matches() && NUM_FIELDS == matcher.groupCount()) {
					matchCount++;
					messages.add(new String[] { matcher.group(0),
							matcher.group(1), matcher.group(2),
							matcher.group(3), matcher.group(4),
							matcher.group(5), matcher.group(6),
							matcher.group(7), matcher.group(8) });
				}
			}
		}
		
		metaData.put("Total Line Count", String.valueOf(totCount));
		metaData.put("Total lines parsed", String.valueOf(matchCount));
		metaData.put("Total bytes read", String.valueOf(totalBytes));
		metaData.put("Average Bytes per line", String.valueOf(totalBytes/totCount));
		
		return messages;
	}
}
