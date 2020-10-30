package org.linuxsenpai.konachan.db;

import android.os.Build;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.util.Date;

@Entity
public class Favorite {
	@PrimaryKey(autoGenerate = true)
	public int uid;

	@ForeignKey(entity = Post.class, parentColumns = {}, childColumns = {"uid"}, onDelete = ForeignKey.CASCADE)
	public int post_uid;
	@ColumnInfo(name = "created")
	public long created;

	public Favorite() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			this.created = Instant.now().getEpochSecond();
		} else {
			Date date = new Date();
			this.created = date.getTime();
		}
	}

	public Date getCreatedDate() {
		return new Date(this.created);
	}
}
