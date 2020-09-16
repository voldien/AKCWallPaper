package org.linuxsenpai.konachan.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
	@Query("SELECT * FROM Tag")
	List<Tag> getAll();

	@Query("SELECT * FROM Tag WHERE uid IN (:userIds)")
	List<Tag> loadAllByIds(int[] userIds);

	@Query("SELECT * FROM Tag WHERE name == (:name) LIMIT 1;")
	Tag getByName(String name);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(Tag... tags);

	@Delete
	void delete(Tag tag);
}
