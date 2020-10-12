package org.linuxsenpai.konachan.activity.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.PostImagesAdapter;
import org.linuxsenpai.konachan.db.Post;

import java.util.List;

//import org.linuxsenpai.konachan.adapter.FavoritesAdapter;

public class FavoriteFragment extends Fragment {

	public static FavoriteFragment newInstance() {
		FavoriteFragment fragment = new FavoriteFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
		View root = inflater.inflate(R.layout.fragment_favorites, container, false);
		RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
		PostImagesAdapter imageAdapter = new PostImagesAdapter("");
		recyclerView.setAdapter(imageAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
		//Adapter
		//	FavoritesAdapter adapter = new FavoritesAdapter();

		favoriteViewModel.getFavorites().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
			@Override
			public void onChanged(List<Post> posts) {
				/*TODO assign the information over to the post recycle image view.  */
				//recyclerView.setAdapter(new FavoritesAdapter(posts));
			}
		});
		//recyclerView.setAdapter(adapter);
		favoriteViewModel.loadFavorites(this.getContext());
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
