package org.linuxsenpai.konachan.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Types;
import java.util.Date;

@Entity
public class Wiki {

	@PrimaryKey
	public int uid;
	public long created_at;
	public long updated_at;
	public String title;
	@ColumnInfo(typeAffinity = Types.BLOB)
	public String body;
	public int updater_id;
	public boolean locked;
	public int version;

	public Date getDate() {
		return new Date(this.created_at);
	}

	public Date getModifedDate() {
		return new Date(this.updated_at);
	}
}
