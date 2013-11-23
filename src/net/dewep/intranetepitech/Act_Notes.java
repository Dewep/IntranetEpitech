package net.dewep.intranetepitech;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Act_Notes extends Activity {
	String url_module;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_notes);
		Global.startup(this);

		Bundle b = getIntent().getExtras();
		this.url_module = b.getString("url_module");

		ListView lw = (ListView) findViewById(R.id.listviewnotes);
		Stock.getInstance().notesInit(this, 0);
		AdapterNotes adapter = Stock.getInstance().notesAdapter();
		this.launch();
		lw.setAdapter(adapter);

		LinearLayout ll = (LinearLayout) findViewById(R.id.edittext);
		ll.setVisibility(View.GONE);
	}

	protected void launch()
	{
		Stock.getInstance().notes_req = 0;

		RecupDonneesNet mnm = new RecupDonneesNet(this, true);
		MyRequest req = new MyRequest();
		req.url = "/module/" + this.url_module + "/note/?format=json";
		req.type = Global.T_NOTES;
		mnm.execute(req);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getMenuInflater().inflate(R.menu.refresh, menu);
		else
			getMenuInflater().inflate(R.menu.home_refresh, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.menu_home:
			Intent parentActivityIntent = new Intent(this, Act_Mes_Notes.class);
			Bundle b = new Bundle();
			b.putString("login", Act_Settings.getLoginActu(this));
			parentActivityIntent.putExtras(b);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(parentActivityIntent);
			return true;
		case R.id.menu_refresh:
			Stock.getInstance().notes = new ArrayList<Note>();
			this.launch();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
