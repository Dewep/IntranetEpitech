package net.dewep.intranetepitech;

import java.util.Comparator;

public class ComparatorNotes implements Comparator<Note> {
	@Override
	public int compare(Note o1, Note o2) {
		return (o2.date.compareTo(o1.date));
	}
}
