package org.linuxsenpai.konachan.fragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.tasks.FetchTagListItemsTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TagListViewModel extends ViewModel {

	private final AtomicBoolean isLoading;
	private MutableLiveData<String> searchQuery;
	private MutableLiveData<List<Tag>> liveData;

	public TagListViewModel() {
		isLoading = new AtomicBoolean(false);
	}

	public LiveData<List<Tag>> getTagList() {
		if (liveData == null)
			liveData = new MutableLiveData<>();
		return liveData;
	}

	public void loadTags(String query) {
		this.searchQuery.setValue(query);
	}


	public void loadTagsOffset(Context context, int offset) {
		if (!isLoading.get()) {
			isLoading.set(true);
			FetchTagListItemsTask fetchTagListItemsTask = new FetchTagListItemsTask(context, this, 25, offset);
			fetchTagListItemsTask.execute(searchQuery.getValue());
		}
		//TODO add so it is called only once.
	}

	public void addTagItems(List<Tag> tags) {
		this.liveData.setValue(tags);
		isLoading.set(false);
	}

	public LiveData<String> getSearchQuery() {
		if (searchQuery == null)
			searchQuery = new MutableLiveData<>();
		return searchQuery;
	}
}
