package org.linuxsenpai.konachan.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Types;
import java.util.Date;

@Entity
public class Wiki implements Parcelable {

	public static final Creator<Wiki> CREATOR = new Creator<Wiki>() {
		@Override
		public Wiki createFromParcel(Parcel in) {
			return new Wiki(in);
		}

		@Override
		public Wiki[] newArray(int size) {
			return new Wiki[size];
		}
	};
	@PrimaryKey
	public int uid;
	public long created_at;
	public long updated_at;
	public String title;
	@ColumnInfo(typeAffinity = Types.BLOB)
	public String body;
	public int updater_id;
	public int version;

	public Wiki() {

	}

	protected Wiki(Parcel in) {
		uid = in.readInt();
		created_at = in.readLong();
		created_at = in.readLong();
		title = in.readString();
		body = in.readString();
		updater_id = in.readInt();
		version = in.readInt();
	}

	public Date getCreatedDate() {
		return new Date(this.created_at);
	}

	public Date getModifedDate() {
		return new Date(this.updated_at);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(uid);
		dest.writeLong(created_at);
		dest.writeLong(updated_at);
		dest.writeString(title);
		dest.writeString(body);
		dest.writeInt(updater_id);
		dest.writeInt(version);
	}

}
