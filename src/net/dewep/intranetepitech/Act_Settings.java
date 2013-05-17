package net.dewep.intranetepitech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Act_Settings extends Activity implements OnClickListener {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_settings);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
		EditText login = (EditText) findViewById(R.id.login);
		login.setText(pref.getString("login", ""));
		Button b = (Button) findViewById(R.id.save_settings);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		TextView info = (TextView) findViewById(R.id.settings_info);
		EditText login = (EditText) findViewById(R.id.login);
		EditText password = (EditText) findViewById(R.id.password);
		if (login.getText().length() < 1)
		{
			info.setText(getResources().getString(R.string.login_trop_court));
		}
		else if (password.getText().length() < 1)
		{
			info.setText(getResources().getString(R.string.mdp_trop_court));
		}
		else
		{
			SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("login", login.getText().toString());
			editor.putString("password", password.getText().toString());
			editor.commit();
			info.setText(getResources().getString(R.string.identifiants_save));
			Stock.getInstance().httpclient = null;
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			startActivity(parentActivityIntent);
		}
	}

	public static String getLogin(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return pref.getString("login", "");
	}

	public static String getPassword(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return pref.getString("password", "");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.menu_home:
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			startActivity(parentActivityIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
