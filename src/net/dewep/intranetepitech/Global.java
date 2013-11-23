package net.dewep.intranetepitech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

public class Global extends Activity {
	static int T_LOGIN = -1;
	static int T_MAIN = 0;
	static int T_MES_NOTES = 1;
	static int T_NOTES = 2;
	static int T_CAL = 3;
	static int T_INSCRIPTIONS = 4;
	static int T_MESSAGES = 5;
	static int T_TOKENS = 6;
	static int T_PROJETS = 7;
	static int T_SUSIES = 8;
	static int T_SUSIE = 9;
	static int T_INSCRIPTION_SUSIE = 10;
	static int T_INSCRIPTION_INSCRIPTIONS = 11;

	public static void startup(Context context)
	{
		if (Act_Settings.getLogin(context).equals("") || Act_Settings.getPassword(context).equals(""))
		{
			Intent intent = new Intent(context, Act_Settings.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		if (Stock.getInstance().httpclient == null)
		{
			Global.login(context);
		}
	}

	public static void login(Context context)
	{
		if (Stock.getInstance().httpclient != null)
			return ;
		Bundle b = new Bundle();
		Intent ActSettings = new Intent(context, Act_Settings.class);
		ActSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		b.putInt("autolog", 1);
		ActSettings.putExtras(b);
		context.startActivity(ActSettings);
	}

	public static String logas()
	{
		return "";
	}
}
