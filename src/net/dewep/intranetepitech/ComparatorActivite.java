package net.dewep.intranetepitech;

import java.util.Comparator;

public class ComparatorActivite implements Comparator<Activite> {
	@Override
	public int compare(Activite o1, Activite o2) {
		if (o1.start_soutenance != null && o2.start_soutenance != null)
			return (o1.start_soutenance.compareTo(o2.start_soutenance));
		if (o1.start_soutenance != null)
			return (o1.start_soutenance.compareTo(o2.start));
		if (o2.start_soutenance != null)
			return (o1.start.compareTo(o2.start_soutenance));
		return (o1.start.compareTo(o2.start));
	}
}
