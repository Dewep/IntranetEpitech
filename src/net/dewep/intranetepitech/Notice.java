package net.dewep.intranetepitech;

import android.text.Html;

public class Notice {
	String titre = null;
	String description = null;
	String date = null;

	public Notice(String titre, String description, String date) {
		this.titre = Html.fromHtml(titre).toString();
		this.description = Html.fromHtml(description).toString();
		this.date = date;
	}

	public String toString()
	{
		return (titre + "\n" + description + "\n" + date);
	}
}
