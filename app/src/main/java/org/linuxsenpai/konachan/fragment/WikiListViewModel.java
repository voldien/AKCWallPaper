package org.linuxsenpai.konachan.fragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.linuxsenpai.konachan.db.Wiki;
import org.linuxsenpai.konachan.tasks.FetchWikiItemsTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WikiListViewModel extends ViewModel {

	private final AtomicBoolean isLoading;
	private MutableLiveData<String> liveData;
	private MutableLiveData<List<Wiki>> liveListData;

	public WikiListViewModel() {
		isLoading = new AtomicBoolean(false);
	}

	public void loadWiki(String query, boolean useCurrent) {
		if (!useCurrent)
			this.liveData.setValue(query);
		else
			this.liveData.setValue(liveData.getValue());
	}

	public LiveData<String> getWikiSearch() {
		if (liveData == null)
			liveData = new MutableLiveData<>();
		return liveData;
	}

	public LiveData<List<Wiki>> getWikiList() {
		if (liveListData == null)
			liveListData = new MutableLiveData<>();
		return liveListData;
	}

	public void loadWikiOffset(Context context, int offset) {
		if (!isLoading.get()) {
			isLoading.set(true);
			FetchWikiItemsTask fetchDisplayImageHolderItem = new FetchWikiItemsTask(context, this, 25, offset);
			fetchDisplayImageHolderItem.execute(liveData.getValue());
		}
		//TODO add so it is called only once.
	}

	public void addWikiItems(List<Wiki> wikis) {
		this.liveListData.setValue(wikis);
		isLoading.set(false);
	}
}
