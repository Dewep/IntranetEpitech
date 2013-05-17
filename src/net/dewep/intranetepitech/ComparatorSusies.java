package net.dewep.intranetepitech;

import java.util.Comparator;

public class ComparatorSusies implements Comparator<Susie> {
	@Override
	public int compare(Susie o1, Susie o2) {
		if (o1.subscribed)
			return (-1);
		if (o2.subscribed)
			return (1);
		return (o1.start.compareTo(o2.start));
	}
}
