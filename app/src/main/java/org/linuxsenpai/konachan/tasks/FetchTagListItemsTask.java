package org.linuxsenpai.konachan.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.fragment.TagListViewModel;

import java.util.List;

public class FetchTagListItemsTask extends AsyncTask<String, Void, List<Tag>> {

	private final Context context;
	private final TagListViewModel adapter;
	private final int length;
	private final int offset;

	public FetchTagListItemsTask(Context context, TagListViewModel tagListViewModel, int length, int offset) {
		this.adapter = tagListViewModel;
		this.context = context;
		this.length = length;
		this.offset = offset;
	}

	@Override
	protected List<Tag> doInBackground(String... strings) {
		return MetaController.getInstance(context).getSearchTag(strings[0], length, offset);
	}

	@Override
	protected void onPostExecute(List<Tag> tags) {
		super.onPostExecute(tags);
		adapter.addTagItems(tags);
	}
}
