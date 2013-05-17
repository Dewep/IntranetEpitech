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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Act_Mes_Projets extends Activity implements OnClickListener {
	int move;
	int days;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.startup(this);

		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_projets);
		Bundle b = getIntent().getExtras();
		this.move = b.getInt("move");
		this.days = b.getInt("days");

		ListView lw = (ListView) findViewById(R.id.listviewprojets);
		Stock.getInstance().projetsInit(this);
		AdapterProjets adapter = Stock.getInstance().projetsAdapter();
		this.launch();
		lw.setAdapter(adapter);

		TextView title = (TextView) findViewById(R.id.date_explain);
		title.setText(ManipulateDate.date_explain(this.move, this.days));
		ImageButton imageprec = (ImageButton) findViewById(R.id.imageprec);
		imageprec.setOnClickListener(this);
		ImageButton imagesuiv = (ImageButton) findViewById(R.id.imagesuiv);
		imagesuiv.setOnClickListener(this);
	}

	protected void launch()
	{
		Stock.getInstance().projets_req = 0;

		RecupDonneesNet mnm = new RecupDonneesNet(this, true);
		MyRequest req = new MyRequest();
		req.url = "https://intra.epitech.eu/module/board/?format=json&" + ManipulateDate.start_end(this.move, this.days);
		req.type = Global.T_PROJETS;
		mnm.execute(req);		
	}

	@Override
	public void onClick(View v) {
		if (((ImageButton) v).getId() == R.id.imageprec)
		{
			Intent intent = new Intent(Act_Mes_Projets.this, Act_Mes_Projets.class);
			Bundle b = new Bundle();
			b.putInt("days", this.days);
			b.putInt("move", this.move - 1);
			intent.putExtras(b);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
		}
		else if (((ImageButton) v).getId() == R.id.imagesuiv)
		{
			Intent intent2 = new Intent(Act_Mes_Projets.this, Act_Mes_Projets.class);
			Bundle b2 = new Bundle();
			b2.putInt("days", this.days);
			b2.putInt("move", this.move + 1);
			intent2.putExtras(b2);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent2);
		}
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
			Stock.getInstance().projets = new ArrayList<Projet>();
			this.launch();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
