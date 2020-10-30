package org.linuxsenpai.konachan.events;

import android.content.Context;
import android.widget.SearchView;

import androidx.preference.PreferenceManager;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.PostImagesAdapter;
import org.linuxsenpai.konachan.adapter.SearchSuggestionAdapter;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.History;
import org.linuxsenpai.konachan.fragment.PostImagesViewModel;
import org.linuxsenpai.konachan.tasks.SearchSuggestionPopulateTask;

import java.util.Calendar;

public class SearchQueryListener implements SearchView.OnQueryTextListener {

	private final Context context;
	private final SearchSuggestionAdapter mSearchSuggestionAdapater;
	private final SearchView searchView;
	private final PostImagesViewModel imageViewModel;
	private final PostImagesAdapter adapter;

	public SearchQueryListener(Context context, SearchView searchView, SearchSuggestionAdapter imageViewModel, PostImagesViewModel postImagesViewModel, PostImagesAdapter adapter) {
		this.context = context;
		this.searchView = searchView;
		this.mSearchSuggestionAdapater = imageViewModel;
		this.imageViewModel = postImagesViewModel;
		this.adapter = adapter;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		this.adapter.clear();
		imageViewModel.loadPost(query, false);
		/*  Update search history.  */
		if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getResources().getString(R.string.key_save_history), false)) {
			History history = new History();
			history.created = Calendar.getInstance().getTime().getTime();
			history.name = query;
			AppDatabase.getAppDatabase(context).historyDao().insertAll(history);
		}

		/*  */
		searchView.clearFocus();
		searchView.onActionViewCollapsed();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length() >= 3) {
			SearchSuggestionPopulateTask searchSuggestionPopulateTask = new SearchSuggestionPopulateTask(this.searchView.getContext(), mSearchSuggestionAdapater, 5);
			searchSuggestionPopulateTask.execute(newText);
		}
		return false;
	}

}
