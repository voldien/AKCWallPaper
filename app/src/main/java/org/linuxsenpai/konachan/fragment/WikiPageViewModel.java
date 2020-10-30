package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Wiki;

public class WikiPageViewModel extends ViewModel {
	// TODO: Implement the ViewModel
	MutableLiveData<Wiki> wiki;

	public LiveData<Wiki> getWikiItem() {
		if (wiki == null)
			wiki = new MutableLiveData<>();
		return wiki;
	}

	public void setWikiItem(Wiki post) {
	}
}
