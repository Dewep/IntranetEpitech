package net.dewep.intranetepitech;

import android.text.Html;

public class Message {
	String titre = null;
	String description = null;
	String date = null;

	public Message(String titre, String description, String date) {
		this.titre = Html.fromHtml(titre).toString();
		this.description = Html.fromHtml(description).toString();
		this.date = date;
	}

	public String toString()
	{
		return (titre + "\n" + description + "\n" + date);
	}
}
