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
	public Map<String, Integer> getDataSet(List<String[]> messages,
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

	private Map<String, Integer> classifyData(List<String[]> messages,
			int colIndex) {
		Map<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		Calendar endCal = Calendar.getInstance();
		try {
			Integer classCount = 0;
			endCal.setTime(dateFormat.parse(messages.get(0)[colIndex]));
			String startHour = String.valueOf(endCal.get(Calendar.HOUR)) + ":" + String.valueOf(endCal.get(Calendar.MINUTE));
			endCal.add(Calendar.HOUR, 1);
			String endHour = String.valueOf(endCal.get(Calendar.HOUR)) + ":" + String.valueOf(endCal.get(Calendar.MINUTE));
			for (String[] strArray : messages) {
				Calendar currentCal = Calendar.getInstance();
				currentCal.setTime(dateFormat.parse(strArray[colIndex]));
				if(currentCal.before(endCal)) {
					classCount++;
				} else {
					dataMap.put(startHour + " - " + endHour, classCount);
					startHour = endHour;
					endCal.add(Calendar.HOUR, 1);
					endHour = String.valueOf(endCal.get(Calendar.HOUR)) + ":" + String.valueOf(endCal.get(Calendar.MINUTE));
					classCount = 0;
				}
			}
			dataMap.put(startHour + " - " + endHour, classCount);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dataMap;
	}

}
