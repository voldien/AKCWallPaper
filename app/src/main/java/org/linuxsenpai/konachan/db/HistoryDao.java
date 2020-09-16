package org.linuxsenpai.konachan.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

	@Query("SELECT * FROM History")
	List<History> getAll();

	@Query("SELECT * FROM History WHERE uid IN (:userIds)")
	List<History> loadAllByIds(int[] userIds);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(History... histories);

	@Delete
	void delete(History history);
}
