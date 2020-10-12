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
	public boolean locked;
	public int version;

	public Wiki() {

	}

	protected Wiki(Parcel in) {

		body = in.readString();
		version = in.readInt();
	}

	public Date getDate() {
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

		dest.writeString(body);
		dest.writeInt(version);
	}

}
