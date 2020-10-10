package org.linuxsenpai.konachan.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post implements Parcelable {

	public static final Parcelable.Creator<Post> CREATOR
			= new Parcelable.Creator<Post>() {
		public Post createFromParcel(Parcel in) {
			return new Post(in);
		}

		public Post[] newArray(int size) {
			return new Post[size];
		}
	};
	@PrimaryKey
	public int uid;
	public String tags;
	public long created_at;
	public String author;
	public String source;
	public int score;
	public int file_size;
	public String file_url;
	public boolean is_shown_in_index;
	public String previewUrl;
	public int actual_preview_width;
	public int actual_preview_height;
	public String sampleUrl;
	public boolean hasChildren;
	public int parent_id;

	//TOOD remove
/*	public ArrayList<TagItem> GetTagObjectList() throws JSONException {
		ArrayList<TagItem> tagItemArrayList = new ArrayList<>();
		String tags = GetTags();
		String[] taglist = TextUtils.split(tags, " ");
		for (String tag : taglist) {
			JSONObject object = new JSONObject();
			tagItemArrayList.add(new TagItem(null));
		}
		return tagItemArrayList;
	}*/
	public String rating;

	private Post(Parcel in) {
		this.uid = in.readInt();
		this.tags = in.readString();
		this.created_at = in.readLong();
		this.author = in.readString();
		this.source = in.readString();
		this.score = in.readInt();
		this.file_size = in.readInt();
		this.file_url = in.readString();
		//this.is_shown_in_index = (boolean)in.readInt();
		this.previewUrl = in.readString();
		this.actual_preview_width = in.readInt();
		this.actual_preview_height = in.readInt();
		this.sampleUrl = in.readString();
		//this.hasChildren = (boolean)in.readInt();
		this.parent_id = in.readInt();
	}

	public Post() {

	}

	public String[] GetTagObjectList() {
		return TextUtils.split(tags, " ");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(uid);
		dest.writeString(tags);
		dest.writeLong(created_at);
		dest.writeString(author);
		dest.writeString(source);
		dest.writeInt(score);
		dest.writeInt(file_size);
		dest.writeString(file_url);
		//dest.writeInt((int)is_shown_in_index);
		dest.writeString(previewUrl);
		dest.writeInt(actual_preview_width);
		dest.writeInt(actual_preview_height);
		dest.writeString(sampleUrl);
		//dest.writeInt(hasChildren);
		dest.writeInt(parent_id);
	}
}
