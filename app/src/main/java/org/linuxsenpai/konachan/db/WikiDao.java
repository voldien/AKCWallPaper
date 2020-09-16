package org.linuxsenpai.konachan.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WikiDao {
	@Query("SELECT * FROM Wiki")
	List<Wiki> getAll();

	@Query("SELECT * FROM Wiki WHERE uid IN (:userIds)")
	List<Wiki> loadAllByIds(int[] userIds);

	@Insert
	void insertAll(Wiki... histories);

	@Delete
	void delete(Wiki history);
}
