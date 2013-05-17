package net.dewep.intranetepitech;

import android.content.Context;
import android.content.Intent;
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
			context.startActivity(intent);
		}
	}

	public static void login(Context context)
	{
		if (Stock.getInstance().httpclient != null)
			return ;
		RecupDonneesNet mnm = new RecupDonneesNet(context, false);
		MyRequest req = new MyRequest();
		req.url = "https://intra.epitech.eu";
		req.type = Global.T_LOGIN;
		mnm.execute(req);
	}
}
