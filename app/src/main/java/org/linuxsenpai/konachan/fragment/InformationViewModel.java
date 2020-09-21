package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Post;

public class InformationViewModel extends ViewModel {
	private MutableLiveData<Post> jsonObjectMutableLiveData;

	public LiveData<Post> getInformation() {
		if (getJsonObjectMutableLiveData() == null)
			this.setJsonObjectMutableLiveData(new MutableLiveData<>());
		return getJsonObjectMutableLiveData();
	}

	public MutableLiveData<Post> getJsonObjectMutableLiveData() {
		return jsonObjectMutableLiveData;
	}

	public void setJsonObjectMutableLiveData(MutableLiveData<Post> jsonObjectMutableLiveData) {
		this.jsonObjectMutableLiveData = jsonObjectMutableLiveData;
	}

	public void loadInformation(Post post) {
		this.jsonObjectMutableLiveData.setValue(post);
	}
}
