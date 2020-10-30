package org.linuxsenpai.konachan.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Wiki;
import org.linuxsenpai.konachan.fragment.WikiListViewModel;

import java.util.List;

public class FetchWikiItemsTask extends AsyncTask<String, Void, List<Wiki>> {
	private final Context context;
	private final WikiListViewModel adapter;
	private final int length;
	private final int offset;

	public FetchWikiItemsTask(Context context, WikiListViewModel adapter, int length, int offset) {
		this.adapter = adapter;
		this.context = context;
		this.length = length;
		this.offset = offset;
	}

	@Override
	protected List<Wiki> doInBackground(String... strings) {
		return MetaController.getInstance(context).getWikiItems(strings[0], length, offset);
	}

	@Override
	protected void onPostExecute(List<Wiki> wikis) {
		super.onPostExecute(wikis);
		adapter.addWikiItems(wikis);
	}
}
