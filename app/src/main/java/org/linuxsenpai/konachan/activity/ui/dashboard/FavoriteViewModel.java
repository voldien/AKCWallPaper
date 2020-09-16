package org.linuxsenpai.konachan.activity.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public FavoriteViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is dashboard fragment");
	}

	public LiveData<String> getText() {
		return mText;
	}
}
