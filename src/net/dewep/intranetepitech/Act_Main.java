package net.dewep.intranetepitech;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Act_Main extends Activity {

	private ListView maListViewPerso;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		Global.startup(this);
		Global.login(this);

		maListViewPerso = (ListView) findViewById(R.id.listviewmenu);
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_messages));
		map.put("img", String.valueOf(R.drawable.ic_menu_infos_rapides));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_journee));
		map.put("img", String.valueOf(R.drawable.ic_menu_ma_journee));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_semaine));
		map.put("img", String.valueOf(R.drawable.ic_menu_ma_semaine));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", "Planning inscriptions");
		map.put("img", String.valueOf(R.drawable.ic_menu_inscriptions));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", "Susie");
		map.put("img", String.valueOf(R.drawable.ic_menu_susie));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_notes));
		map.put("img", String.valueOf(R.drawable.ic_menu_mes_notes));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_projets));
		map.put("img", String.valueOf(R.drawable.ic_menu_mes_projets));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_preferences));
		map.put("img", String.valueOf(R.drawable.ic_menu_settings));
		listItem.add(map);

		map = new HashMap<String, String>();
		map.put("titre", getResources().getString(R.string.m_apropos));
		map.put("img", String.valueOf(R.drawable.ic_menu_netsoul));
		listItem.add(map);

		SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.item_main_menu,
				new String[] {"img", "titre"}, new int[] {R.id.img, R.id.titre});
		maListViewPerso.setAdapter(mSchedule);

		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
				Bundle b = new Bundle();
				if (map.get("titre").equals(getResources().getString(R.string.m_semaine)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Calendrier.class);
					b.putInt("move", 0);
					b.putInt("days", 7);
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_journee)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Calendrier.class);
					b.putInt("move", 0);
					b.putInt("days", 1);
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_notes)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Mes_Notes.class);
					b.putString("login", Act_Settings.getLogin(Act_Main.this));
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_preferences)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Settings.class);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_apropos)))
				{
					Intent intent = new Intent(Act_Main.this, Act_A_Propos.class);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_projets)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Mes_Projets.class);
					b.putInt("move", 0);
					b.putInt("days", 7);
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals("Planning inscriptions"))
				{
					Intent intent = new Intent(Act_Main.this, Act_Inscriptions.class);
					b.putInt("move", 0);
					b.putInt("days", 7);
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals("Susie"))
				{
					Intent intent = new Intent(Act_Main.this, Act_Susies.class);
					b.putInt("move", 0);
					b.putInt("days", 7);
					intent.putExtras(b);
					startActivity(intent);
				}
				else if (map.get("titre").equals(getResources().getString(R.string.m_messages)))
				{
					Intent intent = new Intent(Act_Main.this, Act_Messages.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(Act_Main.this, Act_Settings.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
