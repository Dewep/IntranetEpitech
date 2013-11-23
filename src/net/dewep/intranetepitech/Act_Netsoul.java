package net.dewep.intranetepitech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Act_Netsoul extends Activity implements OnClickListener {
	static Netsoul netsoul = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.act_netsoul);
		Global.startup(this);

		SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
		EditText login = (EditText) findViewById(R.id.login);
		login.setText(pref.getString("login_netsoul", pref.getString("login", "")));
		EditText password = (EditText) findViewById(R.id.password);
		password.setText(pref.getString("password_netsoul", ""));
		netsoul = Netsoul.getInstance();
		/*if (netsoul == null)
		{
			Log.d("netsoul", "new");
			netsoul = new Netsoul();
		}
		else
		{
			Log.d("netsoul", "new");
			netsoul.getStatus();
		}*/
		netsoul.init(mHandler);
		netsoul.getStatus();
		Button butt = (Button) findViewById(R.id.button);
		butt.setOnClickListener(this);
		butt.setText("Connexion");
	}

	@SuppressLint("HandlerLeak")
	final Handler mHandler = new Handler() {
	    public void handleMessage(Message msg) {
			Button butt = (Button) findViewById(R.id.button);
			TextView status = (TextView) findViewById(R.id.status);
			status.setText("Status : " + (String) msg.obj);
	    	if (msg.what == 0)
				butt.setText("Connexion");
	    	else if (msg.what == 1)
				butt.setText("Déconnexion");
	    }
	};

	@Override
	public void onClick(View v) {
		if (((Button) v).getText().equals("Connexion"))
		{
			SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			EditText login = (EditText) findViewById(R.id.login);
			EditText password = (EditText) findViewById(R.id.password);
			editor.putString("login_netsoul", login.getText().toString());
			editor.putString("password_netsoul", password.getText().toString());
			editor.commit();
			netsoul.start(login.getText().toString(), password.getText().toString());
		}
		else
		{
			netsoul.destroy();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.menu_home:
			Intent parentActivityIntent = new Intent(this, Act_Main.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(parentActivityIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
