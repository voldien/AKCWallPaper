package org.linuxsenpai.konachan.Events;

import android.database.Cursor;
import android.widget.SearchView;

import org.linuxsenpai.konachan.adapter.SearchSuggestionAdapter;

public class SearchQuerySuggestionListener implements SearchView.OnSuggestionListener {

	SearchSuggestionAdapter mSearchSuggestionAdapater;
	SearchView searchView;

	public SearchQuerySuggestionListener(SearchView searchView, SearchSuggestionAdapter adapter) {
		this.searchView = searchView;
		this.mSearchSuggestionAdapater = adapter;
	}

	@Override
	public boolean onSuggestionSelect(int position) {
		Cursor cursor = (Cursor) mSearchSuggestionAdapater.getItem(position);
		String txt = cursor.getString(cursor.getColumnIndexOrThrow("name"));
		searchView.setQuery(txt, false);
		return true;
	}

	@Override
	public boolean onSuggestionClick(int position) {
		Cursor cursor = (Cursor) mSearchSuggestionAdapater.getItem(position);
		String txt = cursor.getString(cursor.getColumnIndexOrThrow("name"));
		searchView.setQuery(txt, true);
		return true;
	}
}
