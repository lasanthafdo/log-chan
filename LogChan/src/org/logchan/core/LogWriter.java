package org.logchan.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class LogWriter {
	public static void writeToFile(String fileName, List<String[]> messages, Map<String, Object> metaData) throws IOException {
		if (fileName != null && new File(fileName).createNewFile()) {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			
			for(String[] logEntry: messages) {
				out.println(StringUtils.join(logEntry, ','));
			}
			
			out.println(SystemConstants.META_DATA_HEAD);
			for(String key: metaData.keySet()) {
				Object value = metaData.get(key);
				if(value instanceof String) 
					out.println(key + ":" + value);
			}
			out.println(SystemConstants.META_DATA_END);
			
			out.close();
		}
	}	
}
