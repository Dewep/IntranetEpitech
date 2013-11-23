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
import android.widget.TextView;

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
		TextView t1 = (TextView) findViewById(R.id.dewep);
		t1.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(parentActivityIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if (v.getId() == R.id.github)
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Dewep/IntranetEpitech"));
		else if (v.getId() == R.id.email)
			intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "aurelien.maigret@epitech.eu", null));
		else if (v.getId() == R.id.dewep)
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dewep.net"));
		startActivity(intent);
	}
}
