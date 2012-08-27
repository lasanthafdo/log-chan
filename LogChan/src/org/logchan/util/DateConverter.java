package org.logchan.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
	public static Date valueOf(String date) throws ParseException {
		// Date format for the standard Apache web server log timestamp
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
		return dateFormat.parse(date);
	}
}