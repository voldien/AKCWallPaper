package org.linuxsenpai.konachan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.TagListViewAdapter;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;

import java.util.List;

public class TagListFragment extends SearchableListFragment {

	private static final String ARG_COLUMN_COUNT = "column-count";
	private static final String ARG_TAG_POST_REFERENCE = "post-reference";
	private TagListViewModel mViewModel;
	private int mColumnCount = 1;
	private RecyclerView recyclerView;

	public TagListFragment() {
	}

	public static TagListFragment newInstance(int columnCount, Post post) {
		TagListFragment fragment = new TagListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		args.putParcelable(ARG_TAG_POST_REFERENCE, post);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.searchQueryListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mViewModel.loadTags(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		};



		/*  */
		this.mViewModel = new ViewModelProvider(this).get(TagListViewModel.class);
		this.mViewModel.getSearchQuery().observe(this, new Observer<String>() {
			@Override
			public void onChanged(String s) {
				if (recyclerView.getAdapter() == null)
					recyclerView.setAdapter(new TagListViewAdapter(getContext()));
				TagListViewAdapter adapter = (TagListViewAdapter) recyclerView.getAdapter();
				adapter.clear();
				recyclerView.getLayoutManager().scrollToPosition(0);
				mViewModel.loadTagsOffset(getContext(), 0);
			}
		});
		this.mViewModel.getTagList().observe(this, new Observer<List<Tag>>() {
			@Override
			public void onChanged(List<Tag> tagItems) {
				TagListViewAdapter adapter = (TagListViewAdapter) recyclerView.getAdapter();
				adapter.addItems(tagItems);
				if (refreshview != null)
					refreshview.setRefreshing(false);
			}
		});

		//TODO add support for post reference object.
		Post postReference = null;
		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
			getArguments().getParcelable(ARG_TAG_POST_REFERENCE);
		}

		if (savedInstanceState == null) {
			this.mViewModel.loadTags("");
		} else {
			this.mViewModel.loadTags("");
		}
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

			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
					super.onScrollStateChanged(recyclerView, newState);
					if (!recyclerView.canScrollVertically(1)) {
						mViewModel.loadTagsOffset(getContext(), recyclerView.getAdapter().getItemCount());
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
		TagListViewAdapter adapter = (TagListViewAdapter) recyclerView.getAdapter();
		adapter.clear();
//		mViewModel.loadTags("", true);
	}
}
