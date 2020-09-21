package org.linuxsenpai.konachan.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag {

	@PrimaryKey
	public int uid;
	public String name;
	public int count;
	public int type;

	public TagType getType() {

		switch (this.type) {
			case 0:
				return TagType.Any;
			case 1:
				return TagType.Character;
			case 2:
			case 3:
			default:
				return TagType.Any;
		}
	}
}
