package org.linuxsenpai.konachan.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag implements Parcelable {

	public static final Parcelable.Creator<Tag> CREATOR
			= new Parcelable.Creator<Tag>() {
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}

		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
	@PrimaryKey
	public int uid;
	public String name;
	public int count;
	public int type;

	public Tag() {

	}

	private Tag(Parcel in) {
		this.uid = in.readInt();
		this.name = in.readString();
		this.count = in.readInt();
		this.type = in.readInt();

	}

	public TagType getType() {

		switch (this.type) {
			case 1:
				return TagType.Character;
			case 2:
			case 3:
			case 0:
			default:
				return TagType.Any;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(uid);
		dest.writeString(name);
		dest.writeInt(count);
		dest.writeInt(type);
	}
}
