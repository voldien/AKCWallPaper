package org.linuxsenpai.konachan.activity.ui.favorites;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.preference.SharedPreference;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

	private MutableLiveData<String> mText;
	private MutableLiveData<List<Post>> favorites;

	public FavoriteViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is dashboard fragment");
		favorites = new MutableLiveData<>();
	}

	public LiveData<String> getText() {
		return mText;
	}

	public LiveData<List<Post>> getFavorites() {
		return favorites;
	}

	public void loadFavorites(Context context) {
		SharedPreference sharedPreference = new SharedPreference();
		favorites.setValue(sharedPreference.getFavorites(context));
	}
}
