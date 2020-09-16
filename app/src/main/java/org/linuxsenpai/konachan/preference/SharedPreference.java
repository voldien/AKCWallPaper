package org.linuxsenpai.konachan.preference;

import android.content.Context;

import androidx.annotation.NonNull;

import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.Favorite;
import org.linuxsenpai.konachan.db.Post;

import java.util.ArrayList;
import java.util.List;

public class SharedPreference {

	public SharedPreference() {
		super();
	}

	public boolean isPostFavorite(Context context, @NonNull Post post) {
		ArrayList<Favorite> favorites = (ArrayList<Favorite>) AppDatabase.getAppDatabase(context).favoriteDao().loadAllByIds(new int[]{post.uid});
		return favorites.size() > 0;
	}

	public void addFavorite(Context context, Post postItem) {
		Favorite favorite = new Favorite();
		favorite.post_uid = postItem.uid;
		AppDatabase.getAppDatabase(context).favoriteDao().insertAll(favorite);
	}

	public void removeFavorite(Context context, Post post) {
		List<Favorite> favoriteList = AppDatabase.getAppDatabase(context).favoriteDao().loadAllByIds(new int[]{post.uid});
		if (favoriteList != null && favoriteList.size() > 0) {
			for (Favorite fav :
					favoriteList) {
				AppDatabase.getAppDatabase(context).favoriteDao().delete(fav);
			}

		}
	}

	public List<Post> getFavorites(Context context) {
		List<Favorite> favoriteList = AppDatabase.getAppDatabase(context).favoriteDao().getAll();
		int[] IDIndex = new int[favoriteList.size()];
		for (int i = 0; i < favoriteList.size(); i++) {
			IDIndex[i] = favoriteList.get(i).post_uid;
		}
		return AppDatabase.getAppDatabase(context).postDao().loadAllByIds(IDIndex);

	}

}
