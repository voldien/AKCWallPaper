package org.linuxsenpai.konachan.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class, History.class, Favorite.class, Tag.class, Post.class, Wiki.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase _instance;


	public static AppDatabase getAppDatabase(Context context) {
		if (_instance == null) {
			_instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "konachan-data-api").allowMainThreadQueries().build();
		}
		return _instance;
	}

	public abstract HistoryDao historyDao();

	public abstract FavoriteDao favoriteDao();

	public abstract PostDao postDao();

	public abstract TagDao tagDao();

	public abstract WikiDao wikiDao();

	public abstract NoteDao noteDao();

	/*	public abstract WikiDao*/
}
