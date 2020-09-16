package org.linuxsenpai.konachan.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
	@Query("SELECT * FROM Note")
	List<Note> getAll();

	@Query("SELECT * FROM Note WHERE post_id IN (:userIds)")
	List<Note> loadAllByIds(int[] userIds);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<Note> notes);

	@Delete
	void delete(Note post);
}
