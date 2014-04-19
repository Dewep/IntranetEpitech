package net.dewep.intranetepitech;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Act_Mes_Notes extends Activity implements OnClickListener {
	String login = "";

	@SuppressLint({ "NewApi", "DefaultLocale" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_notes);
		Global.startup(this);

		Bundle b = getIntent().getExtras();
		this.login = (b.getString("login").equals("")) ? Act_Settings.getLoginActu(this) : b.getString("login");
		this.login = this.login.trim().toLowerCase();

		ListView lw = (ListView) findViewById(R.id.listviewnotes);
		Stock.getInstance().notesInit(this, 1);
		AdapterNotes adapter = Stock.getInstance().notesAdapter();
		this.launch();
		lw.setAdapter(adapter);

		Button search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		EditText edittext = (EditText) findViewById(R.id.login);
		if (!this.login.equals(Act_Settings.getLoginActu(this)))
			edittext.setText(this.login);
		edittext.requestFocus();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	protected void launch()
	{
		Stock.getInstance().notes_req = 0;

		RecupDonneesNet mnm = new RecupDonneesNet(this, true);
		MyRequest req = new MyRequest();
		req.url = "/user/" + this.login + "/notes/?format=json";
		req.type = Global.T_MES_NOTES;
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

	@Override
	public void onClick(View v) {
		EditText edittext = (EditText) findViewById(R.id.login);
		Intent intent = new Intent(Act_Mes_Notes.this, Act_Mes_Notes.class);
		Bundle b = new Bundle();
		b.putString("login", edittext.getText().toString());
		intent.putExtras(b);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
