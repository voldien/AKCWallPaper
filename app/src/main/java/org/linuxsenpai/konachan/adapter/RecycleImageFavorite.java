package org.linuxsenpai.konachan.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;

public class RecycleImageFavorite extends RecyclerView.Adapter<PostRecycleImageAdapter.ViewHolder> {


	@Override
	public int getItemViewType(final int position) {
		return R.layout.item_image_view;
	}

	@NonNull
	@Override
	public PostRecycleImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull PostRecycleImageAdapter.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}
}