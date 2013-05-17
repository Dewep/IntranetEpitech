package net.dewep.intranetepitech;

import java.util.ArrayList;

public class InfosCalendrier {
	String jour = null;
	ArrayList<Activite> acts = null;

	public InfosCalendrier(String jour, ArrayList<Activite> acts) {
		this.jour = jour;
		this.acts = acts;
	}
}
