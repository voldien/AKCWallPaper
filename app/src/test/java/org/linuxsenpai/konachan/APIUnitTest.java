package org.linuxsenpai.konachan;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Note;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.db.Wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

public class APIUnitTest {

	@Test
	public void APISearch_() {

	}

	@NotNull
	private String loadFileString(String localResource) throws IOException {

		InputStream inputStream;
		inputStream = this.getClass().getClassLoader().getResourceAsStream(localResource);
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder responseStrBuilder = new StringBuilder();
		String line;
		line = streamReader.readLine();

		do {
			responseStrBuilder.append(line);
			line = streamReader.readLine();
		} while (line != null);

		return responseStrBuilder.toString();
	}

	@NotNull
	@Contract("_ -> new")
	private JSONArray loadJsonArray(String localResource) throws IOException, JSONException {
		return new JSONArray(loadFileString(localResource));
	}

	public void Parse_JSON_No_Throw() {

	}

	@Test(expected = Test.None.class)
	public void Parse_Post_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("post.json").getJSONObject(0);
		Post post = MetaController.convertJson2Post(object);
		Assert.assertNotNull(post);
		Assert.assertEquals(post.uid, 313892);
		Assert.assertEquals(post.tags, 313892);
		Assert.assertEquals(post.created_at, 313892);
		Assert.assertEquals(post.author, 313892);
		Assert.assertEquals(post.source, 313892);
		Assert.assertEquals(post.file_size, 313892);
		Assert.assertEquals(post.file_url, 313892);
		Assert.assertEquals(post.is_shown_in_index, 313892);
		Assert.assertEquals(post.previewUrl, 313892);
		Assert.assertEquals(post.actual_preview_width, 313892);
		Assert.assertEquals(post.actual_preview_height, 313892);
		Assert.assertEquals(post.sampleUrl, 313892);
		Assert.assertEquals(post.hasChildren, 313892);
		Assert.assertEquals(post.parent_id, -1);
		Assert.assertEquals(post.rating, -1);
	}

	@Test(expected = Test.None.class)
	public void Parse_Tag_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("tag.json").getJSONObject(0);
		Tag tag = MetaController.convertJson2Tag(object);

		/*  */
		Assert.assertNotNull(tag);
		Assert.assertEquals(tag.uid, 45);
		Assert.assertEquals(tag.name, "long_hair");
		Assert.assertEquals(tag.count, 93238);
		Assert.assertEquals(tag.type, 0);
	}

	@Test(expected = Test.None.class)
	public void Parse_Note_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("note.json").getJSONObject(0);
		Note note = MetaController.convertJson2Note(object);

		Assert.assertNotNull(note);
		Assert.assertEquals(note.id, 905);
		Assert.assertEquals(note.created_date, 905);
		Assert.assertEquals(note.x, 905);
		Assert.assertEquals(note.y, 905);
		Assert.assertEquals(note.width, 905);
		Assert.assertEquals(note.height, 905);
		Assert.assertEquals(note.post_id, 905);
		Assert.assertEquals(note.body, 905);
		Assert.assertEquals(note.version, 905);
	}

	@Test(expected = Test.None.class)
	public void Parse_Wiki_Item_Correctly() throws IOException, JSONException {
		JSONObject object = loadJsonArray("wiki.json").getJSONObject(0);
		Wiki wiki = MetaController.convertjson2Wiki(object);
		Assert.assertNotNull(wiki);

		/*  */
		Assert.assertEquals(wiki.uid, 905);
		Assert.assertEquals(wiki.created_at, 93238);
		Assert.assertEquals(wiki.updated_at, 0);
		Assert.assertEquals(wiki.title, "long_hair");
		Assert.assertEquals(wiki.body, "body");
		Assert.assertEquals(wiki.updater_id, "updater_id");
		Assert.assertEquals(wiki.locked, "locked");
		Assert.assertEquals(wiki.version, "locked");
	}

	@Test
	public void Post_Parcel_Constructed_Correct_Order(){
		Post post = new Post();
		post.uid = 10;
		post.tags = "";
		post.created_at = 0;
		post.author ="";
		post.source = "";
		post.file_size = 0;

		Parcel parcel = Parcel.obtain();
		post.writeToParcel(parcel, 0);
		/*  Check all the attributes.   */
		Post post1 = Post.CREATOR.createFromParcel(parcel);
		//TODO add
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
		Assert.assertEquals(post.uid, post1.uid);
	}

	@Test
	public void Tag_Parcel_Constructed_Corrected_Order(){
		Tag tag = new Tag();
		tag.uid = 0;
		tag.count = 0;
		tag.name = "";
		tag.type = Tag.TagType.Any.ordinal();

		Parcel parcel = Parcel.obtain();
	}

}
