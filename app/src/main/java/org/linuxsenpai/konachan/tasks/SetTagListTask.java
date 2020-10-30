package org.linuxsenpai.konachan.tasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.fragment.TagListFragment;

import java.util.ArrayList;
import java.util.List;

public class SetTagListTask extends AsyncTask<Post, Void, List<Tag>> {


	private final TagListFragment tagListFragment;

	public SetTagListTask(TagListFragment tagListFragment) {

		this.tagListFragment = tagListFragment;
	}

	@Override
	protected List<Tag> doInBackground(Post... posts) {
		String[] tags = posts[0].GetTagObjectList();
		ArrayList<Tag> tagArrayList = new ArrayList<>();
		for (String tagKey : tags) {
			if (isCancelled())
				break;
			try {
				Tag tag = MetaController.getInstance(tagListFragment.getContext()).getTag(tagKey);
				tagArrayList.add(tag);
			} catch (JSONException ignored) {

			}
		}
		return tagArrayList;
	}

	@Override
	protected void onPostExecute(List<Tag> tags) {
		super.onPostExecute(tags);
		//tagListFragment.setTagList(tags);

	}
}
