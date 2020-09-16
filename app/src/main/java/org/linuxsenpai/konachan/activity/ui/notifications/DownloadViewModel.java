package org.linuxsenpai.konachan.activity.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DownloadViewModel extends ViewModel {

	private MutableLiveData<String> mText;

	public DownloadViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is notifications fragment");
	}

	public LiveData<String> getText() {
		return mText;
	}
}
