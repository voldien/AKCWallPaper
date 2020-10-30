package org.linuxsenpai.konachan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Wiki;

public class WikiPageFragment extends Fragment {

	private static final String ARG_WIKI_OBJECT = "wiki-item-object";
	private WikiPageViewModel mViewModel;
	private SwipeRefreshLayout refreshview;

	public static WikiPageFragment newInstance(Wiki wiki) {
		WikiPageFragment wikiPageFragment = new WikiPageFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(ARG_WIKI_OBJECT, wiki);
		wikiPageFragment.setArguments(bundle);
		return wikiPageFragment;
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {


		return inflater.inflate(R.layout.fragment_wiki_page, container, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = new ViewModelProvider(this).get(WikiPageViewModel.class);

		mViewModel.getWikiItem().observe(this.getViewLifecycleOwner(), new Observer<Wiki>() {
			@Override
			public void onChanged(Wiki wiki) {
				/*  Assign all the information.    */
				TextView textView = getView().findViewById(R.id.text_title);
				TextView bodyTextView = getView().findViewById(R.id.text_body);

				//TODO deal with formating based on the.
				//Description
				// Tag type
				// Usage
				// Additional Resources
				textView.setText(wiki.title);
				bodyTextView.setText(wiki.body);

				/*  TODO Pass images.    */
			}
		});

		if (getArguments() != null) {
			Wiki wiki = getArguments().getParcelable(ARG_WIKI_OBJECT);
			this.mViewModel.setWikiItem(wiki);
		}

		/*  Based on previous state or new state.   */
		if (savedInstanceState == null) {
		} else {
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() != null) {
			this.refreshview = getActivity().findViewById(R.id.swiperefresh);
		}
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.action_information) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
