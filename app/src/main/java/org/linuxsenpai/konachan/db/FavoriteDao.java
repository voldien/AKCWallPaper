package org.linuxsenpai.konachan.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

	@Query("SELECT * FROM Favorite")
	List<Favorite> getAll();

	@Query("SELECT * FROM Favorite WHERE post_uid IN (:userIds)")
	List<Favorite> loadAllByIds(int[] userIds);

	@Insert
	void insertAll(Favorite... favorites);

	@Delete
	void delete(Favorite favorite);
}
