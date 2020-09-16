package org.linuxsenpai.konachan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.linuxsenpai.konachan.R;

public class SearchSuggestionAdapter extends CursorAdapter {

	//TODO add tag database.
	public SearchSuggestionAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.item_search_suggestion, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Find fields to populate in inflated template
		TextView textView = (TextView) view.findViewById(R.id.search_suggestion_text);
		TextView countView = (TextView) view.findViewById(R.id.search_suggestion_count);

		// Extract properties from cursor
		int position = cursor.getPosition();
		int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
		String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));


		textView.setText(name);
		countView.setText(String.valueOf(count));
		//TODO add color
		//textView.setTextColor();

	}
}
