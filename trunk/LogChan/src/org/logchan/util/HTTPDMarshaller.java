package org.logchan.util;

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

public class HTTPDMarshaller implements DataMarshallable {

	@Override
	public Map<Integer, Integer> getDataSet(List<String[]> messages,
			Map<String, Object> metaMap) {
		// TODO Auto-generated method stub
		Vector<Class<?>> colTypes = (Vector<Class<?>>) metaMap
				.get(SystemConstants.COL_DATA_TYPES);
		int colIndex = 0;
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
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		Calendar endCal = Calendar.getInstance();
		try {
			Integer classCount = 0;
			endCal.setTime(dateFormat.parse(messages.get(0)[colIndex]));
			//int startHour = endCal.get(Calendar.HOUR_OF_DAY);  // + ":" + String.valueOf(endCal.get(Calendar.MINUTE))
			endCal.add(Calendar.HOUR_OF_DAY, 1);
			int endHour =endCal.get(Calendar.HOUR_OF_DAY);  // + ":" + String.valueOf(endCal.get(Calendar.MINUTE))
			boolean countAdded = false;
			for (String[] strArray : messages){
				Calendar currentCal = Calendar.getInstance();
				currentCal.setTime(dateFormat.parse(strArray[colIndex]));
				countAdded = false;
				if(currentCal.before(endCal)) {
					classCount++;
				} else {
					if(dataMap.containsKey(endHour)){
						classCount = dataMap.get(endHour) + classCount;
					}
					System.out.println(endHour + " - " + classCount);
					dataMap.put(endHour,classCount);
					countAdded = true;
				//	startHour = endHour;
					endCal.add(Calendar.HOUR_OF_DAY, 1);
					endHour = endCal.get(Calendar.HOUR_OF_DAY);  // + ":" + String.valueOf(endCal.get(Calendar.MINUTE))
					classCount = 0;
				}
			}
			if(!countAdded){
				if(dataMap.containsKey(endHour)){
					classCount = dataMap.get(endHour) + classCount;
				}
				dataMap.put( endHour, classCount);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataMap;
	}

}
