package lbs.goodplace.com.manage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContantsUtils {
	public static String formatData(long time) {
		Date date = new Date(time * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatter.format(date);
	}
	
	public static String formatDataYYYYMMDD(long time) {
		Date date = new Date(time * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
}
