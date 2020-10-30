package org.linuxsenpai.konachan.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Note implements Parcelable {

	public static final Creator<Note> CREATOR = new Creator<Note>() {
		@Override
		public Note createFromParcel(Parcel in) {
			return new Note(in);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}
	};
	@PrimaryKey
	public int id;
	public long created_date;
	public int x;
	public int y;
	public int width;
	public int height;
	@ForeignKey(entity = Post.class, parentColumns = {}, childColumns = {}, onDelete = ForeignKey.CASCADE)
	public int post_id;
	public String body;
	public int version;
	public long modified_date;

	public Note() {

	}

	protected Note(Parcel in) {
		id = in.readInt();
		created_date = in.readLong();
		x = in.readInt();
		y = in.readInt();
		width = in.readInt();
		height = in.readInt();
		post_id = in.readInt();
		body = in.readString();
		version = in.readInt();
	}

	public Date getCreateDate() {
		return new Date(this.created_date);
	}

	public Date getModifiedDate() {
		return new Date(this.modified_date);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(created_date);
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(width);
		dest.writeInt(height);
		dest.writeInt(post_id);
		dest.writeString(body);
		dest.writeInt(version);
	}
}
