package net.dewep.intranetepitech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterMessages extends BaseAdapter
{
	LayoutInflater inflater;
	Context context;

	public AdapterMessages(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Stock.getInstance().messagesGetCount();
	}
	@Override
	public Object getItem(int position) {
		return Stock.getInstance().messagesGetItem(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (Stock.getInstance().messages.size() == 0)
		{
			convertView = inflater.inflate(R.layout.error, null);
			TextView error_txt = (TextView) convertView.findViewById(R.id.error_txt);
			error_txt.setText(context.getString(R.string.aucune_donnee));
		}
		else
		{
			convertView = inflater.inflate(R.layout.item_messages_message, null);

			Notice msg = (Notice) getItem(position);
			if (msg != null) {
				TextView titre = (TextView) convertView.findViewById(R.id.titre);
				TextView description = (TextView) convertView.findViewById(R.id.description);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				titre.setText(msg.titre);
				description.setText(msg.description);
				date.setText(msg.date);
			}
		}
		convertView.invalidate();
		return convertView;
	}
}
