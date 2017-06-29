package de.nms.test.apt.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String createCurrentDateAsString() {
		final Date date = Calendar.getInstance().getTime();
		final DateFormat formatter = new SimpleDateFormat();
		return formatter.format(date);
	}
}
