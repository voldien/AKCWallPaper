package org.linuxsenpai.konachan.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WikiDao {
	@Query("SELECT * FROM Wiki")
	List<Wiki> getAll();

	@Query("SELECT * FROM Wiki WHERE uid IN (:userIds)")
	List<Wiki> loadAllByIds(int[] userIds);

	@Query("SELECT * FROM WIKI WHERE title LIKE (:tag) OR body LIKE (:tag) ORDER BY created_at ASC LIMIT (:length) OFFSET (:offset)")
	List<Wiki> loadAllBySimilarName(String tag, int length, int offset);

	@Query("SELECT * FROM WIKI ORDER BY created_at ASC LIMIT (:length) OFFSET (:offset)")
	List<Wiki> getOffset(int length, int offset);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(Wiki... wikis);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<Wiki> wikis);

	@Delete
	void delete(Wiki wiki);
}
