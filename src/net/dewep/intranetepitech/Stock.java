package net.dewep.intranetepitech;

import java.net.CookieManager;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;

import android.content.Context;

public class Stock {
	private static Stock _instance = null;
	ArrayList<InfosCalendrier> cal;
	ArrayList<Note> notes;
	ArrayList<Projet> projets;
	ArrayList<Notice> messages;
	ArrayList<Susie> susies;
	AdapterCalendrier calAdapter = null;
	AdapterNotes notesAdapter = null;
	AdapterProjets projetsAdapter = null;
	AdapterMessages messagesAdapter = null;
	AdapterInscriptions inscriptionsAdapter = null;
	AdapterSusies susiesAdapter = null;
	int susies_req = 0;
	int cal_req = 0;
	int notes_req = 0;
	int projets_req = 0;
	int messages_req = 0;
	int id_susie = 0;
	HttpClient httpclient = null;
	CookieManager cookiemanager = null;

	public static Stock getInstance() {
		if (_instance == null) {
			synchronized (Stock.class) {
				if (_instance == null) {
					_instance = new Stock();
				}
			}
		}
		return _instance;
	}

	public void inscriptionsInit(Context context)
	{
		inscriptionsAdapter = new AdapterInscriptions(context);
		cal = new ArrayList<InfosCalendrier>();
	}

	public AdapterInscriptions inscriptionsAdapter()
	{
		return (inscriptionsAdapter);
	}

	public AdapterCalendrier calAdapter()
	{
		return (calAdapter);
	}

	public void calInit(Context context)
	{
		calAdapter = new AdapterCalendrier(context);
		cal = new ArrayList<InfosCalendrier>();
	}

	public void calAddElem(InfosCalendrier t) {
		if (cal == null)
			return ;
		cal.add(t);
	}

	public int calGetCount() {
		if (cal == null)
			return (0);
		if (cal.size() < 1 && cal_req == 1)
			return (1);
		int count = 0;
		for (int j = 0; j < cal.size(); j++) {
			count += 1 + cal.get(j).acts.size();
		}
		return count;
	}

	public Object calGetItem(int position) {
		if (cal == null || cal.size() <= position)
			return (null);
		return cal.get(position);
	}

	public AdapterNotes notesAdapter()
	{
		return (notesAdapter);
	}

	public void notesInit(Context context, int type)
	{
		notesAdapter = new AdapterNotes(context, type);
		notes = new ArrayList<Note>();
	}

	public void notesAddElem(Note t) {
		if (notes == null)
			return ;
		notes.add(t);
	}

	public int notesGetCount() {
		if (notes == null)
			return (0);
		if (notes.size() < 1 && notes_req == 1)
			return (1);
		return notes.size();
	}

	public Object notesGetItem(int position) {
		if (notes == null || notes.size() <= position)
			return (null);
		return notes.get(position);
	}

	public AdapterProjets projetsAdapter()
	{
		return (projetsAdapter);
	}

	public void projetsInit(Context context)
	{
		projetsAdapter = new AdapterProjets(context);
		projets = new ArrayList<Projet>();
	}

	public void projetsAddElem(Projet t) {
		if (projets == null)
			return ;
		projets.add(t);
	}

	public int projetsGetCount() {
		if (projets == null)
			return (0);
		if (projets.size() < 1 && projets_req == 1)
			return (1);
		return projets.size();
	}

	public Object projetsGetItem(int position) {
		if (projets == null || projets.size() <= position)
			return (null);
		return projets.get(position);
	}

	public AdapterMessages messagesAdapter()
	{
		return (messagesAdapter);
	}

	public void messagesInit(Context context)
	{
		messagesAdapter = new AdapterMessages(context);
		messages = new ArrayList<Notice>();
	}

	public void messagesAddElem(Notice t) {
		if (messages == null)
			return ;
		messages.add(t);
	}

	public int messagesGetCount() {
		if (messages == null)
			return (0);
		if (messages.size() < 1 && messages_req == 1)
			return (1);
		return messages.size();
	}

	public Object messagesGetItem(int position) {
		if (messages == null || messages.size() <= position)
			return (null);
		return messages.get(position);
	}

	public AdapterSusies susiesAdapter()
	{
		return (susiesAdapter);
	}

	public void susiesInit(Context context, int type)
	{
		susiesAdapter = new AdapterSusies(context, type);
		susies = new ArrayList<Susie>();
	}

	public void susiesAddElem(Susie t) {
		if (susies == null)
			return ;
		susies.add(t);
	}

	public int susiesGetCount() {
		if (susies == null)
			return (0);
		if (susies.size() < 1 && susies_req == 1)
			return (1);
		return susies.size();
	}

	public Object susiesGetItem(int position) {
		if (susies == null || susies.size() <= position)
			return (null);
		return susies.get(position);
	}
}
