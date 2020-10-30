package org.linuxsenpai.konachan.fragment.history;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.History;

public class HistoryViewModel extends ViewModel {

	private LiveData<PagedList<History>> pagedListLiveData;

	public HistoryViewModel() {
	}

	public LiveData<PagedList<History>> getHistory(Context context) {
		if (pagedListLiveData == null)
			pagedListLiveData = new LivePagedListBuilder<>(
					AppDatabase.getAppDatabase(context).historyDao().getAllFromDate(), /* page size */ 20).build();
		return pagedListLiveData;
	}

}
