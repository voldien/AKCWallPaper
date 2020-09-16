package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Tag;

import java.util.List;

public class TagListViewModel extends ViewModel {

	private MutableLiveData<List<Tag>> liveData;

	public LiveData<List<Tag>> getTagList() {
		if (liveData == null)
			liveData = new MutableLiveData<>();
		return liveData;
	}

	public void setTagList(List<Tag> tagItems) {
		this.liveData.setValue(tagItems);
	}
}
