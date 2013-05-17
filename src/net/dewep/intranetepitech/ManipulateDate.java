package net.dewep.intranetepitech;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ManipulateDate {
	public ManipulateDate()
	{
	}

	public static String calc_date(int move, int days, int addition)
	{
		Calendar date = GregorianCalendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		if (days == 7)
			date.add(Calendar.DAY_OF_YEAR, - ((date.get(Calendar.DAY_OF_WEEK) + 5) % 7));
		date.add(Calendar.DAY_OF_YEAR, move * days);
		String start = dateFormat.format(date.getTime());
		date.add(Calendar.DAY_OF_YEAR, days - 1 + addition);
		String end = dateFormat.format(date.getTime());
		return ("start=" + start + "&end=" + end);
	}

	public static String start_end(int move, int days)
	{
		return (ManipulateDate.calc_date(move, days, 0));
	}

	public static String two_weeks()
	{
		return (ManipulateDate.calc_date(-1, 7, 7));
	}

	@SuppressLint("NewApi")
	public static String date_explain(int move, int days)
	{
		Calendar date = GregorianCalendar.getInstance();
		SimpleDateFormat dateFormat = null;
		if (android.os.Build.VERSION.SDK_INT >= 11)
			dateFormat = new SimpleDateFormat("cccccccccccc dd MMMMMMMMMM", Locale.FRANCE);
		else
			dateFormat = new SimpleDateFormat("dd MMMMMMMMMM", Locale.FRANCE);
		if (days == 7)
			date.add(Calendar.DAY_OF_YEAR, - ((date.get(Calendar.DAY_OF_WEEK) + 5) % 7));
		date.add(Calendar.DAY_OF_YEAR, move * days);
		String start = dateFormat.format(date.getTime());
		date.add(Calendar.DAY_OF_YEAR, days - 1);
		String end = dateFormat.format(date.getTime());
		if (start.equals(end))
		{
			char[] stringArray = start.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			start = new String(stringArray);
			return (start);
		}
		return ("Du " + start + " au " + end);
	}

	@SuppressLint("NewApi")
	public static String convert_date(String old)
	{
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			Date date = format.parse(old);
			SimpleDateFormat format_new = null;
			if (android.os.Build.VERSION.SDK_INT >= 11)
				format_new = new SimpleDateFormat("cccccccccccc dd MMMMMMMMMM", Locale.FRANCE);
			else
				format_new = new SimpleDateFormat("dd MMMMMMMMMM", Locale.FRANCE);
			char[] stringArray = format_new.format(date).toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			return (new String(stringArray));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (old);
	}

	@SuppressLint("NewApi")
	public static String convert_date_time(String old)
	{
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			Date date = format.parse(old);
			SimpleDateFormat format_new = null;
			if (android.os.Build.VERSION.SDK_INT >= 11)
				format_new = new SimpleDateFormat("cccccccccccc dd MMMMMMMMMM HH:mm", Locale.FRANCE);
			else
				format_new = new SimpleDateFormat("dd MMMMMMMMMM HH:mm", Locale.FRANCE);
			char[] stringArray = format_new.format(date).toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			return (new String(stringArray));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (old);
	}
}
