package org.logchan.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

public class LogWriter {
	public static void writeToFile(String fileName, List<String[]> messages, Map<String, Object> metaData) throws IOException {
		if (fileName != null && new File(fileName).createNewFile()) {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			Vector<Class<?>> dataTypes = (Vector<Class<?>>)metaData.get(SystemConstants.COL_DATA_TYPES);
			
			out.println(SystemConstants.META_DATA_HEAD);
			for(String key: metaData.keySet()) {
				Object value = metaData.get(key);
				if(!(value instanceof Collection<?>)) 
					out.println(key + ":" + value);
			}
			
			out.print(SystemConstants.COL_DATA_TYPES + ":");
			boolean first = true;
			for(Class<?> dataType: dataTypes) {
				if(first) {
					out.print(dataType.getName());
					first = false;
				}
				else
					out.print("," + dataType.getName());
			}
			out.println();
			out.println(SystemConstants.META_DATA_END);
			
			for(String[] logEntry: messages) {
				String[] clone = logEntry.clone();
				for(int i=0; i < clone.length; i++)
					clone[i] = dataTypes.get(i).getSimpleName() + "[" + clone[i] + "]";
				out.println(StringUtils.join(clone, ','));
			}
			

			
			out.close();
		}
	}	
}
