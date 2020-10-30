package org.linuxsenpai.konachan.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
	@Query("SELECT * FROM Post ORDER BY uid ASC")
	List<Post> getAll();

	@Query("SELECT * FROM Post WHERE uid IN (:userIds)")
	List<Post> loadAllByIds(int[] userIds);

	@Query("SELECT * FROM Post ORDER BY uid LIMIT (:length) OFFSET (:offset)")
	List<Post> getOffset(int length, int offset);

	@Query("SELECT * FROM Post WHERE tags LIKE (:tag) ORDER BY uid DESC LIMIT (:length) OFFSET (:offset)")
	List<Post> getByTag(String tag, int length, int offset);

	@Query("SELECT * FROM Post WHERE tags LIKE (:tag) ORDER BY date(created_at) ASC LIMIT (:num) OFFSET (:offset)")
	List<Post> loadNumLatestCreated(String tag, int offset, int num);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(Post... posts);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<Post> posts);

	@Delete
	void delete(Post post);


}
