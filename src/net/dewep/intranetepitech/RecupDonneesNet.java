package net.dewep.intranetepitech;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.Toast;
import android.widget.Button;

import android.app.Activity;

class MyRequest {
	int type;
	String url;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	int codeRetour;
	String explain_error;
	String response;
	Object obj;
	boolean susie = false;
}

public class RecupDonneesNet extends AsyncTask<MyRequest, Integer, MyRequest> {
	Context context = null;
	private ProgressDialog progressDialog;

	public RecupDonneesNet(Context context, boolean dialog) {
		this.context = context;
		progressDialog = null;
		if (dialog)
		{
			progressDialog = new ProgressDialog(context);
			if (context.getClass().getSimpleName().equals("Act_Settings"))
				progressDialog.setMessage(context.getString(R.string.connexion_en_cours));
			else
				progressDialog.setMessage(context.getString(R.string.chargement_en_cours));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
	}

	protected HttpResponse post(String url, List<NameValuePair> nameValuePairs, boolean withLogas)
	{
		try {
			String short_domaine = "intra.epitech.eu";
			String domaine = "https://" + short_domaine;
	
			HttpPost httppost = new HttpPost(domaine + (withLogas ? Act_Settings.getLogas(this.context) : "") + url);
			//Log.d("LINK", domaine + (withLogas ? Act_Settings.getLogas(this.context) : "") + url);
	
			//httppost.setHeader("Authorization", "Basic " + Base64.encodeToString("aurelien.maigret:xxxxxxxx".getBytes(), Base64.NO_WRAP));
			PackageManager pm = this.context.getPackageManager();
	      	PackageInfo pi;
	        pi = pm.getPackageInfo(this.context.getPackageName(), 0);
			httppost.addHeader("Referer", domaine + "/IntranetEpitech-" + String.valueOf(pi.versionCode) + "/" + pi.versionName);
			httppost.addHeader("api-key", "Dewep.net");
	
			BasicClientCookie cookie = new BasicClientCookie("language", "fr");
			cookie.setPath("/");
			cookie.setDomain(short_domaine);
			((DefaultHttpClient) Stock.getInstance().httpclient).getCookieStore().addCookie(cookie);
	
			if (nameValuePairs != null) {
				//Log.d("PARAM REQ", "PARAMS ENTITY ADD");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}

			//Log.d("REQ EXECUTE", "wef");
			return Stock.getInstance().httpclient.execute(httppost);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected MyRequest doInBackground(MyRequest... params) {
		MyRequest request = (MyRequest) params[0];
		try {
			HttpResponse resp = null;
			if (Stock.getInstance().httpclient == null)
			{
				//Log.d("INIT CONNEXION", "");
				Stock.getInstance().httpclient = new DefaultHttpClient();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("login", Act_Settings.getLogin(this.context)));
				nameValuePairs.add(new BasicNameValuePair("password", Act_Settings.getPassword(this.context)));
				if ((resp = this.post("/user/?format=json", nameValuePairs, false)) == null)
					throw new IOException("this.post is null");
				BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;)
					builder.append(line).append("\n");
				request.response = builder.toString();
				//Log.d("REQ END", request.response);
				JSONObject objs = new JSONObject(request.response);
				Act_Settings.setCanLogas(this.context, false);
				if (objs.has("rights"))
				{
					JSONObject rights = objs.getJSONObject("rights");
					Act_Settings.setCanLogas(this.context, rights.has("logas"));
				}
				request.codeRetour = resp.getStatusLine().getStatusCode();
				//Log.d("testt", request.response);
				if (request.codeRetour != 200)
					return (request);
			}
			if (request.type == Global.T_LOGIN)
			{
				request.codeRetour = 0;
				return (request);
			}

			if (request.susie)
			{
				if (Stock.getInstance().id_susie == 0)
				{
					if ((resp = this.post("/planning/my-schedules?format=json", null, true)) == null)
						throw new IOException("this.post is null");
					BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
					StringBuilder builder = new StringBuilder();
					for (String line = null; (line = reader.readLine()) != null;)
						builder.append(line).append("\n");
					request.response = builder.toString();
					//Log.d("REQ END", request.response);
					try {
						if (request.response.indexOf("[") == -1)
							request.response = "[" + request.response + "]";
						//Log.d("resp susie", request.response);
						JSONArray array = new JSONArray(request.response);
						for (int i = 0; i < array.length(); i++) {
							JSONObject o = array.getJSONObject(i);
							if (!o.isNull("id") && !o.isNull("type") && o.getString("type").equals("susie"))
								Stock.getInstance().id_susie = o.getInt("id");
						}
					} catch (JSONException e) {
						e.printStackTrace();
						request.codeRetour = -2;
						request.response = context.getString(R.string.erreur_parsing);
						return (request);
					}
					request.codeRetour = resp.getStatusLine().getStatusCode();
					//Log.d("testt", request.response);
					if (request.codeRetour != 200)
						return (request);
				}
				if (Stock.getInstance().id_susie == 0)
				{
					request.codeRetour = -2;
					request.response = "Vous n'êtes inscrit à aucune susie actuellement.";
					return (request);
				}
				request.url = "/planning/" + Integer.toString(Stock.getInstance().id_susie) + "/" + request.url;
			}

			if ((resp = this.post(request.url, request.nameValuePairs, true)) == null)
				throw new IOException("this.post is null");
			request.codeRetour = resp.getStatusLine().getStatusCode();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;)
				builder.append(line).append("\n");
			request.response = builder.toString();
			//Log.d("test", request.response);
			if (request.codeRetour != 200)
				return (request);
			if (request.response.indexOf("[") == -1)
				request.response = "[" + request.response + "]";
		} catch (IOException e) {
			e.printStackTrace();
			request.codeRetour = -1;
			request.explain_error = e.getMessage();
		} catch (JSONException e) {
			e.printStackTrace();
			request.codeRetour = -1;
			request.explain_error = e.getMessage();
		}
		return (request);
	}

	protected void openError(String title, String message)
	{
		if (((Activity) this.context).isFinishing())
			return ;
		Stock.getInstance().httpclient = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setTitle(title);
		builder.setMessage(message);
		AlertDialog dialog = builder.create();
		dialog.show();		
	}

	@Override
	protected void onPostExecute(MyRequest result) {
		if (result == null || result.codeRetour == 0)
		{
			try {
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.cancel();
			} catch (Exception e) {
				// nothing
			}
			Intent intent = new Intent(this.context, Act_Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.context.startActivity(intent);
			((Activity) this.context).finish();
			return ;
		}
		if (result.codeRetour == -2)
			openError(context.getString(R.string.erreur), result.response);
		else if (result.codeRetour == 403 && result.type != Global.T_INSCRIPTION_INSCRIPTIONS && result.type != Global.T_INSCRIPTION_SUSIE)
			openError("Error network", context.getString(R.string.erreur_auth));
		else if (result.codeRetour == -1)
			openError("Error network", context.getString(R.string.impossible_acceder_intra));
		else if (result.codeRetour != 200 && result.type != Global.T_INSCRIPTION_INSCRIPTIONS && result.type != Global.T_INSCRIPTION_SUSIE)
			openError("Error network", context.getString(R.string.http_code_erreur) + " #" + Integer.toString(result.codeRetour) + ".");
		else if (result.codeRetour != 200 && (result.type == Global.T_INSCRIPTION_INSCRIPTIONS || result.type == Global.T_INSCRIPTION_SUSIE))
		{
			if (!((Button) result.obj).isEnabled())
			{
				try {
					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.cancel();
				} catch (Exception e) {
					// nothing
				}
				return ;
			}
			if (((Button) result.obj).getText().equals("Inscription"))
				((Button) result.obj).setText("Désinscription");
			else
				((Button) result.obj).setText("Inscription");
			if (result.response != null && (result.response.contains("24") || result.response.contains("pass")))
				openError("Action impossible", "Trop tard...");
			else
				openError("Action impossible", "Vous êtes déjà inscrit sur un autre créneau.");
		}
		else if (result.type == Global.T_PROJETS)
		{
			try {
				JSONArray array = new JSONArray(result.response);
				for (int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					if (!o.isNull("type_acti_code") && o.getString("type_acti_code").equals("proj"))
						Stock.getInstance().projetsAddElem(new Projet(o.getString("acti_title"), o.getString("title_module"), o.getString("begin_acti"), o.getString("end_acti")));
				}
				Collections.sort(Stock.getInstance().projets, new ComparatorProjet());
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur_parsing), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_CAL || result.type == Global.T_INSCRIPTIONS)
		{
			try {
				JSONArray array = new JSONArray(result.response);
				int i, j, k;
				for (i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					Activite act = null;
					//Log.d("rights", o.getString("rights").contains("\"prof_inst\"") ? "true" : "false");
					//Log.d("Event", o.getString("acti_title") + (!o.isNull("title") ? " " + o.getString("title") : ""));
					if (result.type == Global.T_CAL && (!o.isNull("event_registered") || ((!o.isNull("is_rdv") && o.getString("is_rdv").equals("1")) && (!o.isNull("rdv_group_registered") || !o.isNull("rdv_indiv_registered")))) ||
							result.type == Global.T_INSCRIPTIONS && !o.isNull("scolaryear") && !o.isNull("allow_register") && o.getBoolean("allow_register") && o.getBoolean("module_registered") &&
							(o.isNull("event_registered") && (o.isNull("is_rdv") || !o.getString("is_rdv").equals("1")) || o.getString("event_registered").equals("registered")))
					{
						//Log.d("Event add", "yes !");
						JSONObject r = o.getJSONObject("room");
						act = new Activite(o.getString("acti_title") + (!o.isNull("title") ? " " + o.getString("title") : ""), o.getString("titlemodule"), o.getString("start"), o.getString("end"),
								(!r.isNull("code")) ? r.getString("code") : "", o.getString("event_registered"), (o.isNull("is_rdv") || !o.getString("is_rdv").equals("1")) ? o.getBoolean("allow_token") : false,
										(!o.isNull("rdv_group_registered") ? o.getString("rdv_group_registered") : (!o.isNull("rdv_indiv_registered") ? o.getString("rdv_indiv_registered") : "")),
										o.getString("codeacti"), o.getString("scolaryear") + "/" + o.getString("codemodule") + "/" + o.getString("codeinstance") + "/" + o.getString("codeacti") + "/" + o.getString("codeevent"));
					}
					else if (result.type == Global.T_CAL && !o.isNull("rights") && o.getString("rights").contains("\"prof_inst\""))
					{
						JSONObject r = o.getJSONObject("room");
						act = new Activite(o.getString("acti_title") + (!o.isNull("title") ? " " + o.getString("title") : ""), o.getString("titlemodule"), o.getString("start"), o.getString("end"),
								(!r.isNull("code")) ? r.getString("code") : "", "", false, "", o.getString("codeacti"),
										o.getString("scolaryear") + "/" + o.getString("codemodule") + "/" + o.getString("codeinstance") + "/" + o.getString("codeacti") + "/" + o.getString("codeevent"));
					}
					else if (!o.isNull("calendar_type") && !o.isNull("subscribed") && o.getBoolean("subscribed") && result.type != Global.T_INSCRIPTIONS)
					{
						act = new Activite(o.getString("title"), "Calendrier " + o.getString("calendar_type"), o.getString("start"), o.getString("end"), "", "calendar", false, "", o.getString("id"), "");
					}
					if (act != null)
					{
						k = -1;
						for (j = 0; j < Stock.getInstance().cal.size(); j++) {
							if (((InfosCalendrier) Stock.getInstance().calGetItem(j)).jour.equals(o.getString("start").substring(0, 10)))
								k = j;
						}
						if (k == -1)
						{
							ArrayList<Activite> acts = new ArrayList<Activite>();
							acts.add(act);
							Stock.getInstance().calAddElem(new InfosCalendrier(o.getString("start").substring(0, 10), acts));
						}
						else
							((InfosCalendrier)Stock.getInstance().calGetItem(k)).acts.add(act);
					}
				}
				for (j = 0; j < Stock.getInstance().cal.size(); j++) {
					Collections.sort(((InfosCalendrier)Stock.getInstance().calGetItem(j)).acts, new ComparatorActivite());
				}
				Collections.sort(Stock.getInstance().cal, new ComparatorJour());
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_MESSAGES)
		{
			try {
				JSONObject objs = new JSONObject(result.response);
				JSONArray array = objs.getJSONArray("msgs");
				for (int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					if (!o.isNull("title"))
						Stock.getInstance().messagesAddElem(new Notice(o.getString("title"), o.getString("content"), o.getString("date")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_TOKENS)
		{
			try {
				JSONArray array = new JSONArray(result.response);
				JSONObject o = array.getJSONObject(0);
				if (o.isNull("error"))
				{
					Toast.makeText(this.context, "Token validé", Toast.LENGTH_SHORT).show();
				} else if (o.getString("error").equals("failed"))
				{
					openError("Error token", "Va voir l'adm maintenant...");
				} else
				{
					/*if (!((Button) result.obj).isEnabled())
						return ;*/
					((AlertDialog) result.obj).setMessage(Html.fromHtml("<font color='#FF0000'><b>" + o.getString("error") + "</b></font>"));
					((AlertDialog) result.obj).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur_parsing), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_MES_NOTES)
		{
			try {
				JSONObject objs = new JSONObject(result.response);
				JSONArray array = objs.getJSONArray("notes");
				for (int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					if (!o.isNull("codeacti"))
						Stock.getInstance().notesAddElem(new Note(o.getString("title"), o.getString("titlemodule"), Act_Settings.getLogin(this.context), Act_Settings.getLogin(this.context),
								o.getString("final_note"), o.getString("comment"), o.getString("date"),
								o.getString("scolaryear") + "/" + o.getString("codemodule") + "/" + o.getString("codeinstance") + "/" + o.getString("codeacti")));
				}
				Collections.sort(Stock.getInstance().notes, new ComparatorNotes());
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_NOTES)
		{
			try {
				JSONArray array = new JSONArray(result.response);
				for (int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					Stock.getInstance().notesAddElem(new Note(o.getString("title"), "", o.getString("login"), o.getString("user_title"), o.getString("note"),
							o.getString("comment"), o.getString("date"), ""));
				}
				Collections.sort(Stock.getInstance().notes, new ComparatorNotes());
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_SUSIES)
		{
			try {
				JSONObject objs = new JSONObject(result.response);
				JSONArray array = objs.getJSONArray("activities");
				for (int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					JSONObject m = o.getJSONObject("maker");
					Stock.getInstance().susiesAddElem(new Susie(o.getString("id"), o.getString("type"), o.getString("title"), o.getBoolean("subscribed"), o.getInt("registered"),
							o.getInt("nb_place"), o.getString("start"), o.getString("end"), m.getString("login"), m.getString("title"), o.getString("description"), null));
				}
				Collections.sort(Stock.getInstance().susies, new ComparatorSusies());
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}
		else if (result.type == Global.T_SUSIE)
		{
			try {
				JSONObject o = new JSONObject(result.response);
				JSONArray array = o.getJSONArray("logins");
				JSONObject m = o.getJSONObject("maker");
				ArrayList<String> logins = new ArrayList<String>();
				boolean present = false;
				for (int i = 0; i < array.length(); i++) {
					JSONObject objs = array.getJSONObject(i);
					logins.add(objs.getString("login"));
					if (objs.getString("login").equals(Act_Settings.getLogin(context)))
						present = true;
				}
				Stock.getInstance().susiesAddElem(new Susie(o.getString("id"), o.getString("type"), o.getString("title"), present, logins.size(),
						o.getInt("nb_place"), o.getString("start"), o.getString("end"), m.getString("login"), m.getString("title"), o.getString("description"), logins));
			} catch (JSONException e) {
				e.printStackTrace();
				openError(context.getString(R.string.erreur), context.getString(R.string.erreur_parsing));
			}
		}

		if ((result.type == Global.T_PROJETS) && Stock.getInstance().projetsAdapter() != null)
		{
			Stock.getInstance().projets_req = 1;
			Stock.getInstance().projetsAdapter().notifyDataSetChanged();
		}
		else if ((result.type == Global.T_CAL) && Stock.getInstance().calAdapter() != null)
		{
			Stock.getInstance().cal_req = 1;
			Stock.getInstance().calAdapter().notifyDataSetChanged();
		}
		else if ((result.type == Global.T_MESSAGES) && Stock.getInstance().messagesAdapter() != null)
		{
			Stock.getInstance().messages_req = 1;
			Stock.getInstance().messagesAdapter().notifyDataSetChanged();
		}
		else if ((result.type == Global.T_MES_NOTES || result.type == Global.T_NOTES) && Stock.getInstance().notesAdapter() != null)
		{
			Stock.getInstance().notes_req = 1;
			Stock.getInstance().notesAdapter().notifyDataSetChanged();
		}
		else if ((result.type == Global.T_INSCRIPTIONS) && Stock.getInstance().inscriptionsAdapter() != null)
		{
			Stock.getInstance().cal_req = 1;
			Stock.getInstance().inscriptionsAdapter().notifyDataSetChanged();
		}
		else if ((result.type == Global.T_SUSIE || result.type == Global.T_SUSIES) && Stock.getInstance().susiesAdapter() != null)
		{
			Stock.getInstance().susies_req = 1;
			Stock.getInstance().susiesAdapter().notifyDataSetChanged();
		}

		try {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.cancel();
		} catch (Exception e) {
			// nothing
		}
	}
}
