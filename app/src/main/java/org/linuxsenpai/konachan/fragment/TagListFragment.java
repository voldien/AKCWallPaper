package org.linuxsenpai.konachan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.TagListRecyclerViewAdapter;
import org.linuxsenpai.konachan.db.Tag;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TagListFragment extends Fragment {

	private static final String ARG_COLUMN_COUNT = "column-count";
	private TagListViewModel mViewModel;
	private int mColumnCount = 1;
	private RecyclerView recyclerView;

	public TagListFragment() {
	}

	public static TagListFragment newInstance(int columnCount) {
		TagListFragment fragment = new TagListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
		}

		/*  */
		this.mViewModel = new ViewModelProvider(this).get(TagListViewModel.class);
		this.mViewModel.getTagList().observe(this, new Observer<List<Tag>>() {
			@Override
			public void onChanged(List<Tag> tagItems) {
				recyclerView.setAdapter(new TagListRecyclerViewAdapter(tagItems, getContext()));
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tag_list_list, container, false);

		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			recyclerView = (RecyclerView) view;
			if (mColumnCount <= 1) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
			}
		}

		return view;
	}

	public void setTagList(List<Tag> tagList) {
		this.mViewModel.setTagList(tagList);
	}
}
