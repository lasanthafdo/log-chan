package org.logchan.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApacheLogParser {

	public static final String logEntryPattern =  "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
	public static final int NUM_FIELDS = 9;
	
	public List<String[]> parseFile(String fileName) throws IOException{
		List<String[]> messages = new ArrayList<String[]>();
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
	    Pattern p = Pattern.compile(logEntryPattern);
	    Matcher matcher;	    
		while((line = reader.readLine()) != null){
			matcher = p.matcher(line);
			if (matcher.matches() &&  NUM_FIELDS == matcher.groupCount()) {
				messages.add(new String[]{matcher.group(0),matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5),matcher.group(6),matcher.group(7),matcher.group(8)});  
			}
		}
		return messages;
	}
}
