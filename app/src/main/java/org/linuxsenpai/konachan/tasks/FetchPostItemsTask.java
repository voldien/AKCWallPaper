package org.linuxsenpai.konachan.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.fragment.PostImagesViewModel;

import java.util.List;

public class FetchPostItemsTask extends AsyncTask<String, Void, List<Post>> {

	private final Context context;
	private final PostImagesViewModel adapter;
	private final int length;
	private final int offset;

	public FetchPostItemsTask(Context context, PostImagesViewModel adapter, int length, int offset) {
		this.adapter = adapter;
		this.context = context;
		this.length = length;
		this.offset = offset;
	}

	@Override
	protected List<Post> doInBackground(String... strings) {
		return MetaController.getInstance(context).getPostList(strings[0], length, offset);
	}

	@Override
	protected void onPostExecute(List<Post> wikis) {
		super.onPostExecute(wikis);
		adapter.addWikiItems(wikis);
	}
}
