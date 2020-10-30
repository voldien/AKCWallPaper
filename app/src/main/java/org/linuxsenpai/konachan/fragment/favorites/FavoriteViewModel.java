package org.linuxsenpai.konachan.fragment.favorites;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.preference.SharedPreference;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

	private final MutableLiveData<List<Post>> favorites;

	public FavoriteViewModel() {
		favorites = new MutableLiveData<>();
	}

	public LiveData<List<Post>> getFavorites() {
		return favorites;
	}

	public void loadFavorites(Context context) {
		SharedPreference sharedPreference = new SharedPreference();
		favorites.setValue(sharedPreference.getFavoritePosts(context));
	}
}
