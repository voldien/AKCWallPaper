package org.linuxsenpai.konachan.activity.ui.history;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.History;

public class HistoryViewModel extends ViewModel {

	private LiveData<PagedList<History>> pagedListLiveData;
//	private MutableLiveData<List<History>> history;

	public HistoryViewModel() {
//		this.history = new MutableLiveData<>();
/*		pagedListLiveData = new LivePagedListBuilder<>(
				AppDatabase.getAppDatabase(context).historyDao().getAllFromDate(), *//* page size *//* 20).build();*/
	}

	public LiveData<PagedList<History>> getHistory(Context context) {
		if (pagedListLiveData == null)
			pagedListLiveData = new LivePagedListBuilder<>(
					AppDatabase.getAppDatabase(context).historyDao().getAllFromDate(), /* page size */ 20).build();
		return pagedListLiveData;
	}

//	public LiveData<List<History>> getHistoryList() {
//		return this.history;
//	}

//	public void loadHistory(Context context) {
//		this.history.setValue(AppDatabase.getAppDatabase(context).historyDao().getAll());
//	}
}
