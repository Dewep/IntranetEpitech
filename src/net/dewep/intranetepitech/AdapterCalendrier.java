package net.dewep.intranetepitech;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AdapterCalendrier extends BaseAdapter implements OnClickListener
{
	LayoutInflater inflater;
	Context context;

	public AdapterCalendrier(Context context) {
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
			if (act.isAffToken() != 1)
			{
				token.setVisibility(View.GONE);
			}
			else
			{
				token.setId(position);
				token.setOnClickListener(this);
			}
		}
		convertView.invalidate();
		return convertView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() >= getCount() && v.getId() < 0)
			return ;
		final Activite act = getAct(v.getId());
		if (act != null && act.isAffToken() == 1)
		{
			final EditText input = new EditText(this.context);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			final Context context_class = this.context;
			final AlertDialog dialog = new AlertDialog.Builder(this.context)
			.setTitle("Token").setMessage(act.name).setView(input)
			.setPositiveButton("Ok", null).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).create();
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface d) {
					Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (input.getText().length() != 8 || !input.getText().toString().matches("^-?[0-9]+(\\.[0-9]+)?$"))
								dialog.setMessage(Html.fromHtml("<font color='#FF0000'><b>Il doit y avoir 8 chiffres.</b></font>"));
							else
							{
								dialog.dismiss();
								RecupDonneesNet mnm = new RecupDonneesNet(context_class, true);
								MyRequest req = new MyRequest();
								req.url = "/module/" + act.url_event + "/token?format=json";
								req.type = Global.T_TOKENS;
								req.obj = dialog;
								req.nameValuePairs.add(new BasicNameValuePair("token", input.getText().toString()));
								req.nameValuePairs.add(new BasicNameValuePair("rate", "0"));
								req.nameValuePairs.add(new BasicNameValuePair("comment", ""));
								mnm.execute(req);
							}
						}
					});
				}
			});
			input.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						dialog.getButton(Dialog.BUTTON_POSITIVE).performClick();
					}
					return false;
				}
			});
			dialog.show();
			input.requestFocus();
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}
}
