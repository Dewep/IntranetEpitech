package net.dewep.intranetepitech;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class Act_Susie extends Activity {
	String id;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_susie);
		Global.startup(this);

		Bundle b = getIntent().getExtras();
		this.id = b.getString("id");

		ListView lw = (ListView) findViewById(R.id.listviewinscrits);
		Stock.getInstance().susiesInit(this, 1);
		AdapterSusies adapter = Stock.getInstance().susiesAdapter();
		this.launch();
		lw.setAdapter(adapter);
	}

	protected void launch()
	{
		Stock.getInstance().susies_req = 0;

		RecupDonneesNet mnm = new RecupDonneesNet(this, true);
		MyRequest req = new MyRequest();
		req.url = this.id + "/?format=json";
		req.type = Global.T_SUSIE;
		req.susie = true;
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
			Bundle b = new Bundle();
			Intent parentActivityIntent = new Intent(this, Act_Susies.class);
			b.putInt("move", 0);
			b.putInt("days", 7);
			parentActivityIntent.putExtras(b);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(parentActivityIntent);
			return true;
		case R.id.menu_refresh:
			Stock.getInstance().susies = new ArrayList<Susie>();
			this.launch();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
