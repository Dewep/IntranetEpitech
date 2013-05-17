package net.dewep.intranetepitech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdapterNotes extends BaseAdapter
{
	LayoutInflater inflater;
	Context context;
	int perso = 1;

	public AdapterNotes(Context context, int perso) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.perso = perso;
	}

	@Override
	public int getCount() {
		return Stock.getInstance().notesGetCount();
	}
	@Override
	public Object getItem(int position) {
		return Stock.getInstance().notesGetItem(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (Stock.getInstance().notes.size() == 0)
		{
			convertView = inflater.inflate(R.layout.error, null);
			TextView error_txt = (TextView) convertView.findViewById(R.id.error_txt);
			error_txt.setText(context.getString(R.string.aucune_donnee));
		}
		else
		{
			convertView = inflater.inflate(R.layout.item_notes_note, null);

			final Note note = (Note) getItem(position);
			if (note != null) {
				TextView project = (TextView) convertView.findViewById(R.id.note_project);
				TextView mark = (TextView) convertView.findViewById(R.id.note_mark);
				TextView date = (TextView) convertView.findViewById(R.id.note_date);
				TextView comment = (TextView) convertView.findViewById(R.id.note_comment);
				Button voir_notes = (Button) convertView.findViewById(R.id.voir_notes);
				comment.setText(note.comment);
				date.setText(ManipulateDate.convert_date_time(note.date));
				mark.setText("Note : " + note.mark);
				if (perso == 1)
				{
					project.setText(note.project);
					voir_notes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, Act_Notes.class);
							Bundle b = new Bundle();
							b.putString("url_module", note.url_module);
							intent.putExtras(b);
							context.startActivity(intent);
						}
					});
				}
				else
				{
					project.setText(note.name + " (" + note.login + ")");
					voir_notes.setVisibility(View.GONE);
				}
			}
		}
		convertView.invalidate();
		return convertView;
	}
}
