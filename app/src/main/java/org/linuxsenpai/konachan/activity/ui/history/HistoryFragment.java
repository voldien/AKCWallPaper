package org.linuxsenpai.konachan.activity.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.HistoryAdapter;
import org.linuxsenpai.konachan.db.History;

public class HistoryFragment extends Fragment {


	public static HistoryFragment newInstance() {
		HistoryFragment fragment = new HistoryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		HistoryViewModel historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

		View root = inflater.inflate(R.layout.fragment_history, container, false);
		RecyclerView recyclerView = root.findViewById(R.id.listview);
		HistoryAdapter adapter = new HistoryAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
		recyclerView.setAdapter(adapter);
		/*  */
		historyViewModel.getHistory(this.getContext()).observe(getViewLifecycleOwner(), new Observer<PagedList<History>>() {
			@Override
			public void onChanged(PagedList<History> histories) {
				adapter.submitList(histories);
			}
		});

		//historyViewModel.loadHistory(this.getContext());
		return root;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
