package net.dewep.intranetepitech;

import java.util.ArrayList;

public class Susie {
	String id = null;
	String type = null;
	String title = null;
	boolean subscribed = false;
	int registered = 0;
	int nb_place = 0;
	String start = null;
	String end = null;
	String maker_login = null;
	String maker_name = null;
	String description = null;
	ArrayList<String> logins = null;

	public Susie(String id, String type, String title, boolean subscribed, int registered, int nb_place, String start, String end,
			String maker_login, String maker_name, String description, ArrayList<String> logins) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.subscribed = subscribed;
		this.registered = registered;
		this.nb_place = nb_place;
		this.start = start;
		this.end = end;
		this.maker_login = maker_login;
		this.maker_name = maker_name;
		this.description = description;
		this.logins = logins;
	}

	public String toString()
	{
		return ("");
	}
}
