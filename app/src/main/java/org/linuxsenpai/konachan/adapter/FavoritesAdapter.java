package org.linuxsenpai.konachan.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.linuxsenpai.konachan.db.Post;

import java.util.List;

public class FavoritesAdapter extends PostImagesAdapter {

	public FavoritesAdapter(List<Post> posts) {
		super();
		addItems(posts);
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@NonNull
	@Override
	public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		return super.onCreateViewHolder(parent, viewType);
	}
}

