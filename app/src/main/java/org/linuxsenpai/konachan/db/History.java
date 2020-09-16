package org.linuxsenpai.konachan.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
	@PrimaryKey(autoGenerate = true)
	public int uid;
	@ColumnInfo(name = "name")
	public String name;
	@ColumnInfo(name = "created")
	public long created;

}
