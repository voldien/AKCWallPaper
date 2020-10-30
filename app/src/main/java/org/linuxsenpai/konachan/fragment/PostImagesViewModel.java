package org.linuxsenpai.konachan.fragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.tasks.FetchPostItemsTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostImagesViewModel extends ViewModel {

	private final AtomicBoolean isLoading;
	private MutableLiveData<Integer> coloumns;
	private MutableLiveData<String> searchQuery;
	private MutableLiveData<List<Post>> liveListData;

	public PostImagesViewModel() {
		isLoading = new AtomicBoolean(false);
	}

	public LiveData<String> getSearchQuery() {
		if (searchQuery == null)
			this.searchQuery = new MutableLiveData<>();
		return searchQuery;
	}

	public LiveData<List<Post>> getPostList() {
		if (liveListData == null)
			liveListData = new MutableLiveData<>();
		return liveListData;
	}

	public void loadPost(String query, boolean useCurrent) {
		if (!useCurrent)
			this.searchQuery.setValue(query);
		else
			this.searchQuery.setValue(searchQuery.getValue());
	}

	/*	public void search(String query) {
	 *//*  TODO determine if need to update the database.  *//*
		this.searchQuery.setValue(query);
	}*/

	public void loadPostOffset(Context context, int offset) {
		if (!isLoading.get()) {
			isLoading.set(true);
			FetchPostItemsTask fetchDisplayImageHolderItem = new FetchPostItemsTask(context, this, 25, offset);
			fetchDisplayImageHolderItem.execute(searchQuery.getValue());
		}
		//TODO add so it is called only once.
	}

	public void addWikiItems(List<Post> posts) {
		this.liveListData.setValue(posts);
		isLoading.set(false);
	}

	public String getQueryString() {
		return this.searchQuery.getValue();
	}
}
