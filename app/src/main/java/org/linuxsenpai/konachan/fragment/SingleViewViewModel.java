package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Post;

public class SingleViewViewModel extends ViewModel {
	// TODO: Implement the ViewModel
	MutableLiveData<MetaController> metaControllerMutableLiveData;
	MutableLiveData<Post> postItemMutableLiveData;

	public LiveData<Post> getPost() {
		if (postItemMutableLiveData == null)
			this.postItemMutableLiveData = new MutableLiveData<>();
		return postItemMutableLiveData;
	}

	public void setPostItem(Post postItem) {
		this.postItemMutableLiveData.setValue(postItem);
	}
}
