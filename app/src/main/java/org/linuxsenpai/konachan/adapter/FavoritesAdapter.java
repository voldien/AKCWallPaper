package org.linuxsenpai.konachan.adapter;

/*

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

	public FavoritesAdapter(List<Post> posts) {
		super(posts);
	}

	@NonNull
	@Override
	public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(FavoriteViewHolder holder, int position) {
		Post user = getItem(position);
		if (user != null) {
			holder.bindTo(user);
		} else {
			// Null defines a placeholder item - PagedListAdapter will automatically invalidate
			// this row when the actual object is loaded from the database
			holder.clear();
		}
	}

	public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
			new DiffUtil.ItemCallback<Post>() {
				@Override
				public boolean areItemsTheSame(
						@NonNull Post oldUser, @NonNull Post newUser) {
					// User properties may have changed if reloaded from the DB, but ID is fixed
					return oldUser.uid == newUser.uid;
				}

				@Override
				public boolean areContentsTheSame(
						@NonNull Post oldUser, @NonNull Post newUser) {
					// NOTE: if you use equals, your object must properly override Object#equals()
					// Incorrectly returning false here will result in too many animations.
					return false;//oldUser.equals(newUser);
				}
			};

	public class FavoriteViewHolder extends RecyclerView.ViewHolder {
		public FavoriteViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		public void clear() {
		}

		public void bindTo(Post favorite) {

		}
	}
}
*/
