package net.dewep.intranetepitech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterSusies extends BaseAdapter implements OnClickListener
{
	LayoutInflater inflater;
	Context context;
	int type = 0;

	public AdapterSusies(Context context, int type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.type = type;
	}

	@Override
	public int getCount() {
		if (type == 1 && Stock.getInstance().susiesGetCount() != 0 && Stock.getInstance().susies != null && Stock.getInstance().susies.size() != 0)
		{
			if (Stock.getInstance().susies.get(0).logins.size() == 0)
				return (1);
			return (Stock.getInstance().susies.get(0).logins.size());
		}
		return Stock.getInstance().susiesGetCount();
	}
	@Override
	public Object getItem(int position) {
		return Stock.getInstance().susiesGetItem(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (Stock.getInstance().susies.size() == 0)
		{
			convertView = inflater.inflate(R.layout.error, null);
			TextView error_txt = (TextView) convertView.findViewById(R.id.error_txt);
			error_txt.setText(context.getString(R.string.aucune_donnee));
		}
		else if (type == 0)
		{
			convertView = inflater.inflate(R.layout.item_susies_susie, null);

			final Susie susie = (Susie) getItem(position);
			if (susie != null) {
				LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item_susie);
				if (susie.subscribed)
					ll.setBackgroundColor(context.getResources().getColor(R.color.yellow));
				TextView titre = (TextView) convertView.findViewById(R.id.titre);
				TextView maker = (TextView) convertView.findViewById(R.id.maker);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				TextView description = (TextView) convertView.findViewById(R.id.description);
				Button voir_inscrits = (Button) convertView.findViewById(R.id.voir_inscrits);
				titre.setText(susie.title);
				maker.setText("Type : " + susie.type + "\nInscrits : " + String.valueOf(susie.registered) + "/" + String.valueOf(susie.nb_place));
				date.setText(ManipulateDate.convert_date(susie.start.substring(0, 10)) + " : " + susie.start.substring(11, 13) + "h" + susie.start.substring(14, 16) + "-"
						+ susie.end.substring(11, 13) + "h" + susie.end.substring(14, 16));
				description.setText("Par " + susie.maker_name + " (" + susie.maker_login + ")\n"
						+ ((susie.description.equals("")) ? "Aucune description" : susie.description));
				if (this.type == 0)
				{
					voir_inscrits.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, Act_Susie.class);
							Bundle b = new Bundle();
							b.putString("id", susie.id);
							intent.putExtras(b);
							context.startActivity(intent);
						}
					});
				}
				titre.invalidate();
				date.invalidate();
				maker.invalidate();
				description.invalidate();
				voir_inscrits.invalidate();
				ll.invalidate();
			}
		}
		else if (type == 1)
		{
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);

			Susie susie = (Susie) getItem(0);
			if (susie != null) {
				if (position == 0)
				{
					TextView titre = (TextView) ((Activity) this.context).findViewById(R.id.titre);
					TextView date = (TextView) ((Activity) this.context).findViewById(R.id.date);
					TextView maker = (TextView) ((Activity) this.context).findViewById(R.id.maker);
					TextView type = (TextView) ((Activity) this.context).findViewById(R.id.type);
					TextView description = (TextView) ((Activity) this.context).findViewById(R.id.description);
					titre.setText(susie.title);
					type.setText("Type : " + susie.type);
					maker.setText("Inscrits : " + String.valueOf(susie.registered) + "/" + String.valueOf(susie.nb_place));
					date.setText(ManipulateDate.convert_date(susie.start.substring(0, 10)) + " : " + susie.start.substring(11, 13) + "h" + susie.start.substring(14, 16) + "-"
							+ susie.end.substring(11, 13) + "h" + susie.end.substring(14, 16));
					description.setText("Par " + susie.maker_name + " (" + susie.maker_login + ")\n"
							+ ((susie.description.equals("")) ? "Aucune description" : susie.description));
					Button token = (Button) ((Activity) this.context).findViewById(R.id.register);
					token.setOnClickListener(this);
					if (susie.subscribed)
						token.setText("Désinscription");
					else
						token.setText("Inscription");
					titre.invalidate();
					date.invalidate();
					maker.invalidate();
					description.invalidate();
					token.invalidate();
				}
				TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
				if (susie.logins.size() != 0)
				{
					text1.setText(susie.logins.get(position));
				}
				else
				{
					text1.setText("Aucun inscrit");
				}
			}
		}
		convertView.invalidate();
		return convertView;
	}

	@Override
	public void onClick(View v) {
		Susie susie = (Susie) getItem(0);

		RecupDonneesNet mnm = new RecupDonneesNet(this.context, true);
		MyRequest req = new MyRequest();
		req.obj = v;
		req.type = Global.T_INSCRIPTION_SUSIE;
		if (((Button) v).getText().equals("Inscription"))
		{
			susie.subscribed = true;
			((Button) v).setText("Désinscription");
			req.url = susie.id + "/subscribe?format=json";
		}
		else
		{
			susie.subscribed = false;
			((Button) v).setText("Inscription");
			req.url = susie.id + "/unsubscribe?format=json";
		}
		req.susie = true;
		mnm.execute(req);
	}
}
