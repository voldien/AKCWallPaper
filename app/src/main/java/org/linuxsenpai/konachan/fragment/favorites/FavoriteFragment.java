package org.linuxsenpai.konachan.fragment.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.FavoritesAdapter;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.fragment.SearchableListFragment;

import java.util.List;

public class FavoriteFragment extends SearchableListFragment {

	FavoriteViewModel favoriteViewModel;
	private RecyclerView recyclerView;

	public static FavoriteFragment newInstance() {
		FavoriteFragment fragment = new FavoriteFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_favorites, container, false);
		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
		favoriteViewModel.getFavorites().observe(this, new Observer<List<Post>>() {
			@Override
			public void onChanged(List<Post> posts) {
				recyclerView.setAdapter(new FavoritesAdapter(posts));
				refreshview.setRefreshing(false);
			}
		});

		searchQueryListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		};


		favoriteViewModel.loadFavorites(this.getContext());
	}


	@Override
	public void onRefresh() {
		super.onRefresh();
		favoriteViewModel.loadFavorites(this.getContext());
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
