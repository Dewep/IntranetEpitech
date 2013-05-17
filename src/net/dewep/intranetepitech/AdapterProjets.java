package net.dewep.intranetepitech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterProjets extends BaseAdapter
{
	LayoutInflater inflater;
	Context context;

	public AdapterProjets(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Stock.getInstance().projetsGetCount();
	}
	@Override
	public Object getItem(int position) {
		return Stock.getInstance().projetsGetItem(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (Stock.getInstance().projets.size() == 0)
		{
			convertView = inflater.inflate(R.layout.error, null);
			TextView error_txt = (TextView) convertView.findViewById(R.id.error_txt);
			error_txt.setText(context.getString(R.string.aucune_donnee));
		}
		else
		{
			convertView = inflater.inflate(R.layout.item_projets_projet, null);

			Projet proj = (Projet) getItem(position);
			if (proj != null) {
				TextView name = (TextView) convertView.findViewById(R.id.projet_name);
				TextView module = (TextView) convertView.findViewById(R.id.projet_module);
				TextView restant = (TextView) convertView.findViewById(R.id.projet_restant);
				TextView date = (TextView) convertView.findViewById(R.id.projet_date);
				name.setText(proj.name);
				module.setText(proj.module);
				restant.setText("Fin dans " + proj.restant + " jours");
				date.setText("Début: " + proj.start + "\nFin: " + proj.end);
			}
		}
		convertView.invalidate();
		return convertView;
	}
}
