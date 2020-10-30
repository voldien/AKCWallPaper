package org.linuxsenpai.konachan.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class, History.class, Favorite.class, Tag.class, Post.class, Wiki.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase _instance;


	public static AppDatabase getAppDatabase(Context context) {
		if (_instance == null) {
			_instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "konachan-data-api").allowMainThreadQueries().build();
		}
		return _instance;
	}

	public abstract HistoryDao historyDao();    /*  Internal data.  */

	public abstract FavoriteDao favoriteDao();  /*  Internal data.  */

	public abstract PostDao postDao();  /*  Based on the external API.   */

	public abstract TagDao tagDao();  /*  Based on the external API.   */

	public abstract WikiDao wikiDao();  /*  Based on the external API.   */

	public abstract NoteDao noteDao();  /*  Based on the external API.   */
}
