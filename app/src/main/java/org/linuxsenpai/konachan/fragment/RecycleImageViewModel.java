package org.linuxsenpai.konachan.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecycleImageViewModel extends ViewModel {

	private MutableLiveData<Integer> coloumns;
	private MutableLiveData<String> searchQuery;

	public LiveData<String> getSearchQuery() {
		if (searchQuery == null)
			this.searchQuery = new MutableLiveData<>();
		return searchQuery;
	}

	public void search(String query) {
		/*  TODO determine if need to update the database.  */
		this.searchQuery.setValue(query);
	}
}
