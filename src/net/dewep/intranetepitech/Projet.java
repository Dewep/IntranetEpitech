package net.dewep.intranetepitech;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Projet {
	String name = null;
	String module = null;
	Date d_start = null;
	Date d_end = null;
	String start = null;
	String end = null;
	String restant = null;

	public Projet(String name, String module, String start, String end) {
		try {
			this.name = name;
			this.module = module;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			d_start = format.parse(start);
			d_end = format.parse(end);
			SimpleDateFormat format_new = new SimpleDateFormat("dd MMMMMMMMM HH:mm", Locale.FRANCE);
			this.start = format_new.format(d_start);
			this.end = format_new.format(d_end);
			Date now = new Date();
			this.restant = Long.toString((d_end.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String toString()
	{
		return (name + "\n" + module + "\n" + restant + " (" + start + " to " + end + ")");
	}
}
