package net.dewep.intranetepitech;

import java.util.Comparator;

public class ComparatorJour implements Comparator<InfosCalendrier> {
	@Override
	public int compare(InfosCalendrier o1, InfosCalendrier o2) {
		return (o1.jour.compareTo(o2.jour));
	}
}
