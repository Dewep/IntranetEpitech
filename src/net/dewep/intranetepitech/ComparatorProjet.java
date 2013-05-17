package net.dewep.intranetepitech;

import java.util.Comparator;

public class ComparatorProjet implements Comparator<Projet> {
	@Override
	public int compare(Projet o1, Projet o2) {
		return (Integer.parseInt(o1.restant) - Integer.parseInt(o2.restant));
	}
}