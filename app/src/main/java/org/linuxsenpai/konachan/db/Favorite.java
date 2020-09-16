package org.linuxsenpai.konachan.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Favorite {
	@PrimaryKey(autoGenerate = true)
	public int uid;

	@ForeignKey(entity = Post.class, parentColumns = {}, childColumns = {"uid"}, onDelete = ForeignKey.CASCADE)
	public int post_uid;
}
