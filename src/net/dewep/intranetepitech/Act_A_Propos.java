package net.dewep.intranetepitech;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Act_A_Propos extends Activity implements OnClickListener {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_a_propos);
		Global.startup(this);

		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		Button b = (Button) findViewById(R.id.email);
		b.setOnClickListener(this);
		Button b2 = (Button) findViewById(R.id.github);
		b2.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			startActivity(parentActivityIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if (((Button) v).getText().equals("https://github.com/Dewep/IntranetEpitech"))
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Dewep/IntranetEpitech"));
		else
			intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "aurelien.maigret@epitech.eu", null));
		startActivity(intent);
	}
}
