package org.linuxsenpai.konachan;


import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.HistoryDao;
import org.linuxsenpai.konachan.db.NoteDao;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.PostDao;
import org.linuxsenpai.konachan.db.TagDao;
import org.linuxsenpai.konachan.db.WikiDao;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
	private PostDao postDao;
	private HistoryDao historyDao;
	private NoteDao noteDao;
	private TagDao tagDao;
	private WikiDao wikiDao;

	private AppDatabase db;

	@Before
	public void createDb() {
		Context context = ApplicationProvider.getApplicationContext();
		db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
		postDao = db.postDao();
	}

	@After
	public void closeDb() {
		db.close();
	}

	@Test
	public void writeUserAndReadInList() {
/*		JSONObject object = loadJsonArray("tag.json").getJSONObject(0);
		Tag tag = MetaController.convertJson2Tag(object);*/


		Post post = new Post();
		post.uid = 212;
		postDao.insertAll(post);
		List<Post> posts = postDao.loadAllByIds(new int[]{post.uid});
		//assertThat(post, equalTo(user));
	}


	@Test
	public void FavoritePost() {

	}

}
