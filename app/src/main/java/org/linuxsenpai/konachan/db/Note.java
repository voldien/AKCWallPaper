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
	public int created_date;
	public int x;
	public int y;
	public int width;
	public int height;
	@ForeignKey(entity = Post.class, parentColumns = {}, childColumns = {}, onDelete = ForeignKey.CASCADE)
	public int post_id;
	public String body;
	public int version;

	public Note() {

	}

	protected Note(Parcel in) {
		id = in.readInt();
		created_date = in.readInt();
		x = in.readInt();
		y = in.readInt();
		width = in.readInt();
		height = in.readInt();
		post_id = in.readInt();
		body = in.readString();
		version = in.readInt();
	}

	public Date getDate() {
		return new Date(this.created_date);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(created_date);
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(width);
		dest.writeInt(height);
		dest.writeInt(post_id);
		dest.writeString(body);
		dest.writeInt(version);
	}

	//https://konachan.com/note.json?post_id=312859
	//[{"id":7396,"created_at":"2020-08-12T18:10:52.623Z","updated_at":"2020-08-12T18:10:52.623Z","creator_id":156425,"x":2219,"y":176,"width":150,"height":150,"is_active":true,"post_id":312859,"body":"Yonagi Kei\n(Act-Age)","version":1},
}
