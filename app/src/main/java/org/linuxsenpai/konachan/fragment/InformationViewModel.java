package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Post;

public class InformationViewModel extends ViewModel {
	MutableLiveData<Post> jsonObjectMutableLiveData;

	public LiveData<Post> getInformation() {
		if (jsonObjectMutableLiveData == null)
			this.jsonObjectMutableLiveData = new MutableLiveData<>();
		return jsonObjectMutableLiveData;
	}
}
