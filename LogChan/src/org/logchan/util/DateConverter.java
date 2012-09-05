package org.logchan.util;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
	
	public static Date valueOf(String date) throws InvocationTargetException {
		String apacheFormat = "dd/MMM/yyyy:HH:mm:ss Z";
		return DateConverter.valueOf(date, apacheFormat);
	}

	public static Date valueOf(String date, String format)
			throws InvocationTargetException {
		// Date format for the standard Apache web server log timestamp
		DateFormat dateFormat;
		try {
			dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(date);
		} catch (ParseException e) {
			if (date.matches("\\d{10}\\.\\d{3}")) {
				Double dblDate = Double.valueOf(date) * 1000;
				long timeInMillis = (Math.round(dblDate));
				return new Date(timeInMillis);
			} else {
				throw new InvocationTargetException(e);
			}
		}
	}
}
