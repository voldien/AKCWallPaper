package org.linuxsenpai.konachan.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxsenpai.konachan.BuildConfig;
import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.Note;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.db.Wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MetaController {
	static public final String TAG = "Meta";
	public static MetaController controller;
	private ArrayList<JSONArray> postItem;
	private ArrayList<Integer> pageIndex;
	private final AtomicInteger page;
	private int pageSize = 50;
	private Context context;
	private SharedPreferences preferences;

	//TODO determine to make a instance supported.
	private MetaController(@NonNull Context context) {
		postItem = new ArrayList<>(2);
		pageIndex = new ArrayList<>(2);
		pageIndex.add(0);
		pageIndex.add(1);
		postItem.add(new JSONArray());
		postItem.add(new JSONArray());
		this.page = new AtomicInteger(0);

		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		AppDatabase database = AppDatabase.getAppDatabase(context);
		this.context = context;
	}

	public static MetaController getInstance(@NonNull Context context) {
		if (controller == null) {
			controller = new MetaController(context);
		}
		return controller;
	}

	public static Post convertJson2Post(@NonNull JSONObject object) throws JSONException {
		Post post_entry = new Post();
		post_entry.uid = object.getInt("id");
		post_entry.tags = object.getString("tags");
		post_entry.created_at = object.getLong("created_at");
		post_entry.author = object.getString("author");
		post_entry.file_size = object.getInt("file_size");
		post_entry.source = object.getString("source");
		post_entry.score = object.getInt("score");
		post_entry.file_url = object.getString("file_url");
		post_entry.previewUrl = object.getString("preview_url");
		post_entry.sampleUrl = object.getString("sample_url");
		post_entry.hasChildren = object.getBoolean("has_children");
		post_entry.is_shown_in_index = object.getBoolean("is_shown_in_index");
		post_entry.actual_preview_width = object.getInt("actual_preview_width");
		post_entry.actual_preview_height = object.getInt("actual_preview_height");
		if (!object.isNull("parent_id"))
			post_entry.parent_id = object.getInt("parent_id");
		else
			post_entry.parent_id = -1;
		post_entry.rating = object.getString("rating");
		return post_entry;
	}

	public static Tag convertJson2Tag(@NonNull JSONObject object) throws JSONException {
		Tag tag = new Tag();
		tag.uid = object.getInt("id");
		tag.name = object.getString("name");
		tag.count = object.getInt("count");
		tag.type = object.getInt("type");
		return tag;
	}

	public static Note convertJson2Note(@NonNull JSONObject object) throws JSONException {
		Note note = new Note();
		note.id = object.getInt("id");
		//TODO convert string formated to unix code.
//		note.date  = object.getInt("created_at");
		note.x = object.getInt("x");
		note.y = object.getInt("y");
		note.width = object.getInt("width");
		note.height = object.getInt("height");
		note.post_id = object.getInt("post_id");
		note.body = object.getString("body");
		note.version = object.getInt("version");
		return note;
	}

	public static Wiki convertjson2Wiki(@NonNull JSONObject object) throws JSONException {
		Wiki wiki = new Wiki();
		wiki.uid = object.getInt("id");
		//TODO convert string formated to unix code.
/*		Date created_date = DateFormat.parse(object.getString("created_at"));
		Date updated_at =DateFormat.parse(object.getString("updated_at"));
		wiki.created_at  = created_date.getTime();
		wiki.updated_at =  updated_at.getTime();*/
		wiki.title = object.getString("title");
		wiki.body = object.getString("body");
		wiki.updater_id = object.getInt("updater_id");
		wiki.locked = object.getBoolean("locked");
		wiki.version = object.getInt("version");
		return wiki;
	}

	private String getSearchOptions() {
		return "rating:safe&";
	}


	public void loadTags(String pattern, int offset) {

	}

	public void loadPost(String tag, int offset) {
		/*  Update internal page buffer if out of range.    */
		int page_index = offset / pageSize;
		AppDatabase database = AppDatabase.getAppDatabase(context);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);

		synchronized (page) {
			//if (!lookup.containsKey(page_index)) {
			if (offset >= Math.max(0, page_index * pageSize - 1)) {
				/*  Compute post query. */
				//PreferenceManager.getDefaultSharedPreferences(this.context).getInt()
				final String rating = getSearchOptions();

				@SuppressLint("DefaultLocale") String queryUrl = String.format("post.json?tags=%s+%s&limit=%s&page=%d", tag, rating, pageSize, page_index);  //TODO add the safe mode and etc.
				JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
				if (array.length() != pageSize) {
					/*  Last page.  */
				}
				page.incrementAndGet();

				/*  Update the database.    */
				List<Post> postList = new ArrayList<>(array.length());
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject post_json = array.getJSONObject(i);
						Post postContainer = convertJson2Post(post_json);
						postList.add(postContainer);
					} catch (JSONException ignored) {
					}
				}
				if (BuildConfig.DEBUG) {
					Log.d(TAG, array.toString());
					Log.d(TAG, String.valueOf(postList.size()));
				}
				database.postDao().insertAll(postList);
			}
		}
	}

	public List<Note> getNotes(Post postItem) throws JSONException {
		ArrayList<Note> noteItems = new ArrayList<>();
		int id = postItem.uid;
		@SuppressLint("DefaultLocale")
		String queryUrl = String.format("note.json?post_id=%d", id);  //TODO add the safe mode and etc.
		JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
		for (int i = 0; i < array.length(); i++) {
			Note item = convertJson2Note(array.getJSONObject(i));
			noteItems.add(item);
		}
		AppDatabase.getAppDatabase(context).noteDao().insertAll(noteItems);
		return noteItems;
	}

	public Cursor<Post> getPostItems(@NonNull String tag) {
		return new Cursor<>(this, tag, API.POST);
	}

	public Cursor<Tag> getTagItems(@NonNull String tag) {
		return new Cursor<>(this, tag, API.TAG);
	}

	public Cursor<Wiki> getWikiItems(@NonNull String tag) {
		return new Cursor<>(this, tag, API.WIKI);
	}

	public int getCount() {
		return (page.get() + 2) * pageSize;
	}

	private boolean needUpdate() {
		return false;
	}


	public Post getPost(@NonNull String tag, int offset) {
		if (BuildConfig.DEBUG && offset < 0) {
			throw new AssertionError("Assertion failed");
		}
		Post post = null;

		/*  */
		String QueryTag = String.format("%%%s%%", tag);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);
		if (useCache) {
			if (tag.length() > 0)
				post = AppDatabase.getAppDatabase(context).postDao().getByTag(QueryTag, offset);
			else
				post = AppDatabase.getAppDatabase(context).postDao().getOffset(offset);
		}

		if (post == null) {
			loadPost(tag, offset);

			/*  Second attempt. */
			//TODO improve the logic to prevent calling the same code block twice!.
			if (tag.length() > 0)
				post = AppDatabase.getAppDatabase(context).postDao().getByTag(QueryTag, offset);
			else
				post = AppDatabase.getAppDatabase(context).postDao().getOffset(offset);

			if (post == null)
				throw new RuntimeException(String.format("Could not find post: %s offset: %d", tag, offset));
		}
		return post;
	}

	public Tag getTag(@NonNull String tagkey) throws JSONException {
		if (BuildConfig.DEBUG && tagkey.length() <= 0) {
			throw new AssertionError("Assertion failed");
		}

		Tag tag = AppDatabase.getAppDatabase(context).tagDao().getByName(tagkey);
		if (tag == null) {

			/*  Download and cache the entry.   */
			String url = String.format("tag.json?name=%s*&limit=1&page=1&order=name", tagkey);
			JSONArray jsonArray = Network.GetJsonObjectQuery(this.context, url);
			tag = convertJson2Tag(jsonArray.getJSONObject(0));
			AppDatabase.getAppDatabase(context).tagDao().insertAll(tag);

			/*  */
			tag = AppDatabase.getAppDatabase(context).tagDao().getByName(tagkey);
			if (tag == null)
				throw new RuntimeException(String.format("Failed fetching tag: %s", tagkey));
		}
		return tag;
	}
}
