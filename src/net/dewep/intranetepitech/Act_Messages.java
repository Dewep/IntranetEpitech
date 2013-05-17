package net.dewep.intranetepitech;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class Act_Messages extends Activity {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_messages);
		Global.startup(this);

		ListView lw = (ListView) findViewById(R.id.listviewmessages);
		Stock.getInstance().messagesInit(this);
		AdapterMessages adapter = Stock.getInstance().messagesAdapter();
		this.launch();
		lw.setAdapter(adapter);
	}

	protected void launch()
	{
		Stock.getInstance().messages_req = 0;

		RecupDonneesNet mnm = new RecupDonneesNet(this, true);
		MyRequest req = new MyRequest();
		req.url = "https://intra.epitech.eu/user/notification/message?format=json&lang=fr";
		req.type = Global.T_MESSAGES;
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
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			startActivity(parentActivityIntent);
			return true;
		case R.id.menu_refresh:
			Stock.getInstance().messages = new ArrayList<Message>();
			this.launch();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
