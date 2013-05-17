package net.dewep.intranetepitech;

public class Note {
	String project = null;
	String module = null;
	String login = null;
	String name = null;
	String mark = null;
	String comment = null;
	String date = null;
	String url_module = null;

	public Note(String project, String module, String login, String name, String mark, String comment, String date, String url_module) {
		this.project = project;
		this.module = module;
		this.login = login;
		this.name = name;
		this.mark = mark;
		this.comment = comment;
		this.date = date;
		this.url_module = url_module;
	}

	public String toString()
	{
		return (project + "\n" + login + " : " + mark + "\n" + date + "\n" + comment);
	}
}
