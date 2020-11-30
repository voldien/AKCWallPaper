package org.linuxsenpai.konachan.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.activity.MainActivity;

//TODO rename later
public abstract class SearchableListFragment extends Fragment implements MainActivity.OnAboutDataReceivedListener {

	protected SearchView searchView;

	@Override
	public void onDataReceived(Bundle searcBundle) {

	}

	protected SwipeRefreshLayout refreshview;
	protected SearchView.OnQueryTextListener searchQueryListener;

	protected void detachSearchView() {
		if (this.searchView != null)
			searchView.setOnQueryTextListener(null);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() != null) {
			this.refreshview = getActivity().findViewById(R.id.swiperefresh);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem m = menu.findItem(R.id.action_searchview);
		if (m != null)
			this.searchView = (SearchView) menu.findItem(R.id.action_searchview).getActionView();
	}

	public void onRefresh() {

	}

	@Override
	public void onResume() {
		super.onResume();
		if (searchView != null)
			searchView.setOnQueryTextListener(this.searchQueryListener);
		if (refreshview != null) {
			refreshview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					SearchableListFragment.this.onRefresh();
				}
			});
			refreshview.setEnabled(true);
		}
	}

	@Override
	public void onDetach() {
		detachSearchView();
		super.onDetach();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		detachSearchView();
		super.onPause();
	}
}
