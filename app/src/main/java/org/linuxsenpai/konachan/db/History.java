package org.linuxsenpai.konachan.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class History {
	@PrimaryKey(autoGenerate = true)
	public int uid;
	@ColumnInfo(name = "name")
	public String name;
	@ColumnInfo(name = "created")
	public long created;

	public Date getDate() {
		return new Date(this.created);
	}

}
