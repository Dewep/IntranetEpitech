package net.dewep.intranetepitech;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdapterInscriptions extends BaseAdapter implements OnClickListener
{
	LayoutInflater inflater;
	Context context;

	public AdapterInscriptions(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Stock.getInstance().calGetCount();
	}
	@Override
	public Object getItem(int position) {
		return getAct(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	protected Activite getAct(int position)
	{
		ArrayList<InfosCalendrier> ics = Stock.getInstance().cal;
		int index = 0;
		while (position > ics.get(index).acts.size())
		{
			position -= ics.get(index).acts.size() + 1;
			index++;
		}
		if (position == 0)
			return (null);
		return (ics.get(index).acts.get(position - 1));
	}

	protected String getJour(int position)
	{
		ArrayList<InfosCalendrier> ics = Stock.getInstance().cal;
		int index = 0;
		while (position > ics.get(index).acts.size())
		{
			position -= ics.get(index).acts.size() + 1;
			index++;
		}
		if (position == 0)
			return (ics.get(index).jour);
		return ("");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activite act = null;
		String txt = null;

		convertView = inflater.inflate(R.layout.error, null);
		TextView error_txt = (TextView) convertView.findViewById(R.id.error_txt);
		error_txt.setText(context.getString(R.string.aucune_donnee));
		if (Stock.getInstance().cal.size() == 0)
		{
		}
		else if (!(txt = getJour(position)).equals(""))
		{
			convertView = inflater.inflate(R.layout.item_calendrier_jour, null);

			TextView jour = (TextView) convertView.findViewById(R.id.jour);
			jour.setText(ManipulateDate.convert_date(txt));
		}
		else if ((act = getAct(position)) != null)
		{
			convertView = inflater.inflate(R.layout.item_calendrier_activites, null);

			TextView titre = (TextView) convertView.findViewById(R.id.activite_titre);
			TextView infos = (TextView) convertView.findViewById(R.id.activite_infos);
			Button token = (Button) convertView.findViewById(R.id.token);
			titre.setText(act.start + " - " + act.end + "\n" + act.name);
			infos.setText(act.toString());
			token.setId(position);
			token.setOnClickListener(this);
			if (act.registered.equals("registered"))
				token.setText("Désinscription");
			else
				token.setText("Inscription");
		}
		convertView.invalidate();
		return convertView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() >= getCount() && v.getId() < 0)
			return ;
		Activite act = getAct(v.getId());

		RecupDonneesNet mnm = new RecupDonneesNet(this.context, true);
		MyRequest req = new MyRequest();
		req.obj = v;
		req.type = Global.T_INSCRIPTION_INSCRIPTIONS;
		if (act.registered.equals("registered"))
		{
			act.registered = "";
			((Button) v).setText("Inscription");
			req.url = "/module/" + act.url_event + "/unregister?format=json";
		}
		else
		{
			act.registered = "registered";
			((Button) v).setText("Désinscription");
			req.url = "/module/" + act.url_event + "/register?format=json";
		}
		mnm.execute(req);
	}
}
