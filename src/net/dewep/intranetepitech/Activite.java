package net.dewep.intranetepitech;

public class Activite {
	String name = null;
	String module = null;
	String start = null;
	String end = null;
	String registered = null;
	String room = null;
	String start_soutenance = null;
	String end_soutenance = null;
	String codeacti = null;
	String url_event = null;
	int token = 0;

	public Activite(String name, String module, String start, String end, String room, String registered, boolean token, String soutenance, String codeacti, String url_event) {
		this.name = name;
		this.module = module;
		this.start = start;
		this.end = end;
		this.room = room.substring(room.lastIndexOf("/") + 1);
		this.registered = registered;
		this.codeacti = codeacti;
		this.url_event = url_event;
		this.token = (token == true && registered.equals("registered")) ? 1 : 0;
		if (soutenance != null && soutenance.length() == 39)
		{
			this.start_soutenance = soutenance.substring(11, 16);
			this.end_soutenance = soutenance.substring(31, 36);
		}
		cleanDate();
	}

	public int isAffToken()
	{
		if (this.token == 1)
			return (1);
		return (0);
	}

	public void cleanDate()
	{
		if (start.substring(0, 11).equals(end.substring(0, 11)))
		{
			start = start.substring(11, 16);
			end = end.substring(11, 16);
		}
		else
		{
			start = start.substring(0, 16);
			end = end.substring(0, 16);
		}
	}

	@Override
	public String toString()
	{
		String res = "";
		if (start_soutenance != null && end_soutenance != null)
			res += "Heure de passage : " + start_soutenance + " à " + end_soutenance + "\n";
		res += module + "\n";
		if (!room.equals(""))
			res += "Salle : " + room + "\n";
		return (res);
	}
}
