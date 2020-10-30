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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MetaController {
	static public final String TAG = "Meta";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static MetaController controller;
	private final AtomicInteger page;
	private final int pageSize = 50;    //TODO be replaced by the length for each methods.
	private final Context context;
	private final SharedPreferences preferences;

	private MetaController(@NonNull Context context) {
		this.page = new AtomicInteger(0);
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;
	}

	public static MetaController getInstance(@NonNull Context context) {
		if (controller == null)
			controller = new MetaController(context);
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
		try {
			Date date;
			String date_string = object.getString("created_at");
			String updated_date_string = object.getString("updated_at");
			date = formatter.parse(date_string);
			note.created_date = Objects.requireNonNull(date).getTime();
			date = formatter.parse(updated_date_string);
			note.modified_date = Objects.requireNonNull(date).getTime();
		} catch (ParseException ignored) {
			note.created_date = -1;
			note.modified_date = -1;
		}
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
		try {
			Date date;
			String created_date_string = object.getString("created_at");
			String updated_date_string = object.getString("updated_at");
			date = formatter.parse(created_date_string);
			wiki.created_at = Objects.requireNonNull(date).getTime();
			date = formatter.parse(updated_date_string);
			wiki.updated_at = Objects.requireNonNull(date).getTime();
		} catch (ParseException ignored) {
			wiki.created_at = -1;
			wiki.updated_at = -1;
		}
		wiki.title = object.getString("title");
		wiki.body = object.getString("body");
		wiki.updater_id = object.getInt("updater_id");
		wiki.version = object.getInt("version");
		return wiki;
	}

	private String getSearchOptions(Context context) {
		return "rating:safe&";
	}

	public List<Tag> loadTags(String pattern, int offset) {
		/*  Update internal page buffer if out of range.    */
		int page_index = offset / pageSize;
		AppDatabase database = AppDatabase.getAppDatabase(context);

		List<Tag> tagList = null;
		synchronized (page) {
			//if (!lookup.containsKey(page_index)) {
			if (0 >= Math.max(0, page_index * pageSize - 1)) {
				/*  Compute post query. */
				//PreferenceManager.getDefaultSharedPreferences(this.context).getInt()
				@SuppressLint("DefaultLocale") String queryUrl = String.format("tag.json?name=%s*&limit=%d&page=%d&order=name", pattern, pageSize, page_index);  //TODO add the safe mode and etc.
				JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
				if (array.length() != pageSize) {
					/*  Last page.  */
				}
				page.incrementAndGet();

				/*  Update the database.    */
				tagList = new ArrayList<>(array.length());
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject wiki_json = array.getJSONObject(i);
						Tag json2Tag = convertJson2Tag(wiki_json);
						tagList.add(json2Tag);
					} catch (JSONException ignored) {
					}
				}
				if (BuildConfig.DEBUG) {
					Log.d(TAG, array.toString());
					Log.d(TAG, "Tag size: " + tagList.size());
				}
				database.tagDao().insertAll(tagList);
			}
		}
		return tagList;
	}

	public List<Post> loadPost(String tag, int offset) {
		/*  Update internal page buffer if out of range.    */
		int page_index = offset / pageSize;
		AppDatabase database = AppDatabase.getAppDatabase(context);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);

		List<Post> postList = null;
		synchronized (page) {
			//if (!lookup.containsKey(page_index)) {
			if (offset >= Math.max(0, page_index * pageSize - 1)) {
				/*  Compute post query. */
				//PreferenceManager.getDefaultSharedPreferences(this.context).getInt()
				final String rating = getSearchOptions(context);

				@SuppressLint("DefaultLocale") String queryUrl = String.format("post.json?tags=%s+%s&limit=%s&page=%d", tag, rating, pageSize, page_index);  //TODO add the safe mode and etc.
				JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
				if (array.length() != pageSize) {
					/*  Last page.  */
				}
				page.incrementAndGet();

				/*  Update the database.    */
				postList = new ArrayList<>(array.length());
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
		return postList;
	}

	public List<Wiki> loadWiki(String tag, int length, int offset) {
		/*  Update internal page buffer if out of range.    */
		int page_index = offset / pageSize;
		AppDatabase database = AppDatabase.getAppDatabase(context);
		List<Wiki> wikiList = null;
		synchronized (page) {
			//if (!lookup.containsKey(page_index)) {
			if (offset >= Math.max(0, page_index * pageSize - 1)) {
				/*  Compute post query. */
				//PreferenceManager.getDefaultSharedPreferences(this.context).getInt()
				@SuppressLint("DefaultLocale") String queryUrl = String.format("wiki.json?query=%s&limit=%s&page=%d", tag, pageSize, page_index);  //TODO add the safe mode and etc.
				JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
				if (array.length() != pageSize) {
					/*  Last page.  */
				}
				page.incrementAndGet();

				/*  Update the database.    */
				wikiList = new ArrayList<>(array.length());
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject wiki_json = array.getJSONObject(i);
						Wiki postContainer = convertjson2Wiki(wiki_json);
						wikiList.add(postContainer);
					} catch (JSONException ignored) {
					}
				}
				if (BuildConfig.DEBUG) {
					Log.d(TAG, array.toString());
					Log.d(TAG, "Wiki size: " + wikiList.size());
				}
				database.wikiDao().insertAll(wikiList);
			}
		}
		return wikiList;
	}

	public List<Note> loadNotes(Post post) {
		return loadNotes(post.uid);
	}

	public List<Note> loadNotes(int post_id) {
		/*  Update internal page buffer if out of range.    */
		int page_index = 0 / pageSize;
		AppDatabase database = AppDatabase.getAppDatabase(context);

		List<Note> wikiList = null;
		synchronized (page) {
			//if (!lookup.containsKey(page_index)) {
			if (0 >= Math.max(0, page_index * pageSize - 1)) {
				/*  Compute post query. */
				//PreferenceManager.getDefaultSharedPreferences(this.context).getInt()
				@SuppressLint("DefaultLocale")
				String queryUrl = String.format("note.json?post_id=&%d", post_id);  //TODO add the safe mode and etc.
				JSONArray array = Network.GetJsonObjectQuery(context, queryUrl);
				if (array.length() != pageSize) {
					/*  Last page.  */
				}
				page.incrementAndGet();

				/*  Update the database.    */
				wikiList = new ArrayList<>(array.length());
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject wiki_json = array.getJSONObject(i);
						Note postContainer = convertJson2Note(wiki_json);
						wikiList.add(postContainer);
					} catch (JSONException ignored) {
					}
				}
				if (BuildConfig.DEBUG) {
					Log.d(TAG, array.toString());
					Log.d(TAG, "Wiki size: " + wikiList.size());
				}
				database.noteDao().insertAll(wikiList);
			}
		}
		return wikiList;
	}

	private boolean needUpdate() {
		return false;
	}

	public Post getPost(@NonNull String tag, int offset) {
		return getPostList(tag, 1, offset).get(0);

	}

	public List<Post> getPostList(@NonNull String tag, int length, int offset) {
		if (BuildConfig.DEBUG && offset < 0) {
			throw new AssertionError("Assertion failed");
		}
		List<Post> postList = null;

		/*  */
		String QueryTag = String.format("%%%s%%", tag);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);
		if (useCache) {
			if (tag.length() > 0)
				postList = AppDatabase.getAppDatabase(context).postDao().getByTag(QueryTag, length, offset);
			else
				postList = AppDatabase.getAppDatabase(context).postDao().getOffset(length, offset);
		}

		if (postList == null) {
			postList = loadPost(tag, offset);

			/*  Second attempt. */
			//TODO improve the logic to prevent calling the same code block twice!.
/*			if (tag.length() > 0)
				postList = AppDatabase.getAppDatabase(context).postDao().getByTag(QueryTag,length, offset);
			else
				postList = AppDatabase.getAppDatabase(context).postDao().getOffset(length, offset);*/

			if (postList == null)
				throw new RuntimeException(String.format("Could not find post: %s offset: %d", tag, offset));
		}
		return postList;
	}

	public List<Tag> getSearchTag(String query, int length, int offset) {
		if (BuildConfig.DEBUG && offset < 0) {
			throw new AssertionError("Assertion failed");
		}
		List<Tag> tagList = null;

		/*  */
		String QueryTag = String.format("%%%s%%", query);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);
		if (useCache) {
			if (query.length() > 0)
				tagList = AppDatabase.getAppDatabase(context).tagDao().getBySimilarName(QueryTag, length, offset);
			else
				tagList = AppDatabase.getAppDatabase(context).tagDao().getOffset(length, offset);
		}

		if (tagList == null) {
			tagList = loadTags(query, offset);
			/*  Second attempt. */
			//TODO improve the logic to prevent calling the same code block twice!.
/*
			if (query.length() > 0)
				tagList = AppDatabase.getAppDatabase(context).tagDao().getBySimilarName(QueryTag, length, offset);
			else
				tagList = AppDatabase.getAppDatabase(context).tagDao().getOffset(length, offset);
*/

			if (tagList == null)
				throw new RuntimeException(String.format("Could not find tags: %s length: %d offset: %d", query, length, offset));
		}
		return tagList;
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

	public List<Note> getNotes(Post post) {
		return getNotes(post.uid);
	}

	public List<Note> getNotes(int postID) {

		List<Note> notes = null;

		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);
		if (useCache) {
			notes = AppDatabase.getAppDatabase(context).noteDao().loadAllByIds(postID);
		}
		if (notes == null) {
			notes = loadNotes(postID);
			//notes = AppDatabase.getAppDatabase(context).noteDao().loadAllByIds(postID);

			if (notes == null)
				throw new RuntimeException(String.format("Could not find notes for post: %d", postID));
		}
		return notes;
	}

	public List<Wiki> getWikiItems(String tag, int length, int offset) {
		List<Wiki> wikiList = null;

		/*  */
		String QueryTag = String.format("%%%s%%", tag);
		boolean useCache = preferences.getBoolean(context.getString(R.string.key_use_cache), false);
		if (useCache) {
			if (tag.length() > 0)
				wikiList = AppDatabase.getAppDatabase(context).wikiDao().loadAllBySimilarName(QueryTag, length, offset);
			else
				wikiList = AppDatabase.getAppDatabase(context).wikiDao().getOffset(length, offset);
		}

		if (wikiList == null) {
			wikiList = loadWiki(tag, length, offset);

/*			if (tag.length() > 0)
				wiki = AppDatabase.getAppDatabase(context).wikiDao().loadAllBySimilarName(QueryTag, length, offset);
			else
				wiki = AppDatabase.getAppDatabase(context).wikiDao().getOffset(length, offset);*/

			if (wikiList == null)
				throw new RuntimeException(String.format("Could not find wiki: %s offset: %d", tag, offset));
		}
		return wikiList;

	}
}
