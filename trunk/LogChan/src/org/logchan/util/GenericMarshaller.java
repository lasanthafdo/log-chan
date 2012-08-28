package org.logchan.util;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.logchan.core.SystemConstants;

import com.jshift.util.collections.linkedhash.LinkedHashMap;

public class GenericMarshaller implements DataMarshallable {

	@Override
	public Map<Integer, Integer> getDataSet(List<String[]> messages,
			Map<String, Object> metaMap) {
		Vector<Class<?>> colTypes = (Vector<Class<?>>) metaMap
				.get(SystemConstants.COL_DATA_TYPES);
		int colIndex = (Integer)metaMap.get(SystemConstants.TIMESTAMP_COL);
		for (Class<?> type : colTypes) {
			if (type.equals(Date.class)) {
				colIndex = colTypes.indexOf(type);
			}
		}
		
		return classifyData(messages, colIndex);
	}
	
	private Map<Integer, Integer> classifyData(List<String[]> messages,
			int colIndex) {
		Map<Integer, Integer> dataMap = new LinkedHashMap<Integer, Integer>();
		Calendar endCal = Calendar.getInstance();
		try {
			Integer classCount = 0;
			endCal.setTime(DateConverter.valueOf((messages.get(0)[colIndex])));
			endCal.add(Calendar.HOUR_OF_DAY, 1);
			int endHour =endCal.get(Calendar.HOUR_OF_DAY);  
			boolean countAdded = false;
			for (String[] strArray : messages){
				Calendar currentCal = Calendar.getInstance();
				currentCal.setTime(DateConverter.valueOf((strArray[colIndex])));
				countAdded = false;
				if(currentCal.before(endCal)) {
					classCount++;
				} else {
					if(dataMap.containsKey(endHour)){
						classCount = dataMap.get(endHour) + classCount;
					}
					dataMap.put(endHour,classCount);
					countAdded = true;
					endCal.add(Calendar.HOUR_OF_DAY, 1);
					endHour = endCal.get(Calendar.HOUR_OF_DAY); 
					classCount = 0;
				}
			}
			if(!countAdded){
				if(dataMap.containsKey(endHour)){
					classCount = dataMap.get(endHour) + classCount;
				}
				dataMap.put( endHour, classCount);
			}
			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return dataMap;
	}
	

}
