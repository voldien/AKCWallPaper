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

	@Query("SELECT * FROM Tag WHERE name LIKE (:queryTag) LIMIT (:length) OFFSET (:offset)")
	List<Tag> getBySimilarName(String queryTag, int length, int offset);

	@Query("SELECT * FROM Tag ORDER BY uid LIMIT (:length) OFFSET (:offset)")
	List<Tag> getOffset(int length, int offset);

	@Query("SELECT * FROM Tag WHERE name == (:name) LIMIT 1;")
	Tag getByName(String name);


	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(Tag... tags);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<Tag> tagList);

	@Delete
	void delete(Tag tag);


}
