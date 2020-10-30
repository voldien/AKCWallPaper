package org.linuxsenpai.konachan.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.HistoryAdapter;
import org.linuxsenpai.konachan.db.History;
import org.linuxsenpai.konachan.fragment.SearchableListFragment;

public class HistoryFragment extends SearchableListFragment {

	private RecyclerView recyclerView;
	private HistoryAdapter adapter;
	private HistoryViewModel historyViewModel;

	public static HistoryFragment newInstance() {
		HistoryFragment fragment = new HistoryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, container, false);
		recyclerView = view.findViewById(R.id.listview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.searchQueryListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		};
		historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
		adapter = new HistoryAdapter();
		/*  */
		historyViewModel.getHistory(this.getContext()).observe(this, new Observer<PagedList<History>>() {
			@Override
			public void onChanged(PagedList<History> histories) {
				if (recyclerView.getAdapter() == null)
					recyclerView.setAdapter(adapter);
				adapter.submitList(histories);
				refreshview.setRefreshing(false);
			}
		});
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
