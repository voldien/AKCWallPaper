package org.linuxsenpai.konachan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.WikiListViewAdapter;
import org.linuxsenpai.konachan.db.Wiki;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WikiListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WikiListFragment extends SearchableListFragment {

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_COLUMN_COUNT = "column-count";
	private static final String SAVED_STATE_QUERY_KEY_STRING = "wiki-search-query";
	private static final String SAVED_STATE_SCROLL_POS_KEY = "wiki-scroll-position";
	private WikiListViewModel mViewModel;
	private RecyclerView recyclerView;
	private int mColumnCount = 1;

	public WikiListFragment() {
		// Required empty public constructor
	}

	public static WikiListFragment newInstance(int columnCount) {
		WikiListFragment fragment = new WikiListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.searchQueryListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				WikiListViewAdapter adapter = (WikiListViewAdapter) recyclerView.getAdapter();
				adapter.clear();
				mViewModel.loadWiki(query, false);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		};

		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
		}

		/*  */
		this.mViewModel = new ViewModelProvider(this).get(WikiListViewModel.class);
		this.mViewModel.getWikiSearch().observe(this, new Observer<String>() {
			@Override
			public void onChanged(String query) {
				recyclerView.setAdapter(new WikiListViewAdapter(getContext()));
				mViewModel.loadWikiOffset(getContext(), 0);
				recyclerView.getLayoutManager().scrollToPosition(0);
				if (searchView != null)
					searchView.setQuery(query, false);
			}
		});
		/*  On new list data received.   */
		this.mViewModel.getWikiList().observe(this, new Observer<List<Wiki>>() {
			@Override
			public void onChanged(List<Wiki> wikis) {
				WikiListViewAdapter adapter = (WikiListViewAdapter) recyclerView.getAdapter();
				adapter.addItemList(wikis);
				refreshview.setRefreshing(false);
			}
		});

		/*  Based on previous state or new state.   */
		if (savedInstanceState == null) {
			this.mViewModel.loadWiki("", false);
		} else {
			this.mViewModel.loadWiki(savedInstanceState.getString(SAVED_STATE_QUERY_KEY_STRING), false);
			this.recyclerView.scrollToPosition(savedInstanceState.getInt(SAVED_STATE_SCROLL_POS_KEY));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wiki_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			recyclerView = (RecyclerView) view;
			if (mColumnCount <= 1) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
			}

			/*  Assign event when reaching the last item to start loading next data.    */
			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
					super.onScrollStateChanged(recyclerView, newState);

					if (!recyclerView.canScrollVertically(1)) {
						mViewModel.loadWikiOffset(getContext(), recyclerView.getAdapter().getItemCount());
						Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();

					}
				}
			});
		}

		return view;
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		WikiListViewAdapter adapter = (WikiListViewAdapter) recyclerView.getAdapter();
		adapter.clear();
		mViewModel.loadWiki("", true);
	}


	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(SAVED_STATE_QUERY_KEY_STRING, searchView.getQuery().toString());
	}
}
