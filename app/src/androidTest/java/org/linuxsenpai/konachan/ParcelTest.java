package org.linuxsenpai.konachan;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxsenpai.konachan.db.Note;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.db.TagType;
import org.linuxsenpai.konachan.db.Wiki;

@RunWith(AndroidJUnit4.class)
public class ParcelTest {

	@Test
	public void Post_Parcel_Constructed_Correct_Order() {
		Post post = new Post();
		post.uid = 10;
		post.tags = "Anime Cat";
		post.created_at = 0;
		post.author = "Cat";
		post.source = "";
		post.file_size = 0;

		Parcel parcel = Parcel.obtain();
		post.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		/*  Check all the attributes.   */
		Post post1 = Post.CREATOR.createFromParcel(parcel);

		/*  Check the result is equal.   */
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.tags, post1.tags);
		Assert.assertEquals(post.created_at, post1.created_at);
		Assert.assertEquals(post.author, post1.author);
		Assert.assertEquals(post.source, post1.source);
		Assert.assertEquals(post.score, post1.score);
		Assert.assertEquals(post.file_size, post1.file_size);
		Assert.assertEquals(post.file_url, post1.file_url);
		Assert.assertEquals(post.is_shown_in_index, post1.is_shown_in_index);
		Assert.assertEquals(post.previewUrl, post1.previewUrl);
		Assert.assertEquals(post.actual_preview_width, post1.actual_preview_width);
		Assert.assertEquals(post.actual_preview_height, post1.actual_preview_height);
		Assert.assertEquals(post.sampleUrl, post1.sampleUrl);
		Assert.assertEquals(post.hasChildren, post1.hasChildren);
		Assert.assertEquals(post.parent_id, post1.parent_id);
	}

	@Test
	public void Tag_Parcel_Constructed_Corrected_Order() {
		Tag tag = new Tag();
		tag.uid = 246123;
		tag.count = 5235672;
		tag.name = "Anime";
		tag.type = TagType.Any.ordinal();

		Parcel parcel = Parcel.obtain();
		tag.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		/*  Check all the attributes.   */
		Tag tag1 = Tag.CREATOR.createFromParcel(parcel);

		/*  Check the result is equal.   */
		Assert.assertEquals(tag.uid, tag1.uid);
		Assert.assertEquals(tag.name, tag1.name);
		Assert.assertEquals(tag.count, tag1.count);
		Assert.assertEquals(tag.type, tag1.type);
	}

	@Test
	public void Note_Parcel_Constructed_Corrected_Order(){
		Note note = new Note();

		Parcel parcel = Parcel.obtain();
		note.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		Note note1 = Note.CREATOR.createFromParcel(parcel);

		/*  Check the result is equal.   */
		Assert.assertEquals(note.id, note1.id);
		Assert.assertEquals(note.created_date, note1.created_date);
		Assert.assertEquals(note.x, note1.x);
		Assert.assertEquals(note.y, note1.y);
		Assert.assertEquals(note.width, note1.width);
		Assert.assertEquals(note.height, note1.height);
		Assert.assertEquals(note.post_id, note1.post_id);
		Assert.assertEquals(note.body, note1.body);
		Assert.assertEquals(note.version, note1.version);
	}

	public void Wiki_ParcelConstructed_Corrected_Order(){
		Wiki wiki = new Wiki();

		Parcel parcel = Parcel.obtain();
		wiki.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		Wiki wiki1 = Wiki.CREATOR.createFromParcel(parcel);
		Assert.assertEquals(wiki.uid, wiki1.uid);
		Assert.assertEquals(wiki.created_at, wiki1.created_at);
		Assert.assertEquals(wiki.updated_at, wiki1.updated_at);
		Assert.assertEquals(wiki.title, wiki1.title);
		Assert.assertEquals(wiki.body, wiki1.body);
		Assert.assertEquals(wiki.updater_id, wiki1.updater_id);
		Assert.assertEquals(wiki.locked, wiki1.locked);
		Assert.assertEquals(wiki.version, wiki1.version);

	}
}
