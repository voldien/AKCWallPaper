package org.linuxsenpai.konachan;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Note;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.db.TagType;
import org.linuxsenpai.konachan.db.Wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class APIUnitTest {

	@Test
	public void APISearch_() {

	}

	//TODO make part of the application and reference it.
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

	@Test
	public void Parse_JSON_No_Throw() {

	}

	//TODO resolve date creation.
	@Test()
	public void Parse_Post_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("post.json").getJSONObject(0);
		Post post = MetaController.convertJson2Post(object);
		Assert.assertNotNull(post);
		Assert.assertEquals(post.uid, 313892);
		Assert.assertEquals(post.tags, "arknights qtian tomimi_(arknights)");
		Assert.assertEquals(post.author, "BattlequeenYume");
		Assert.assertEquals(post.file_size, 1738489);
		Assert.assertTrue(post.is_shown_in_index);
		Assert.assertEquals(post.actual_preview_width, 300);
		Assert.assertEquals(post.actual_preview_height, 212);
		Assert.assertFalse(post.hasChildren);
		Assert.assertEquals(post.parent_id, -1);
		Assert.assertEquals(post.rating, "q");
	}

	@Test()
	public void Parse_Tag_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("tag.json").getJSONObject(0);
		Tag tag = MetaController.convertJson2Tag(object);

		/*  */
		Assert.assertNotNull(tag);
		Assert.assertEquals(tag.uid, 45);
		Assert.assertEquals(tag.name, "long_hair");
		Assert.assertEquals(tag.count, 93238);
		Assert.assertEquals(tag.type, 0);
		Assert.assertEquals(tag.getType(), TagType.General);
	}

	@Test()
	public void Parse_Note_Item_Correctly() throws JSONException, IOException {
		JSONObject object = loadJsonArray("note.json").getJSONObject(0);
		Note note = MetaController.convertJson2Note(object);

		Assert.assertNotNull(note);
		Assert.assertEquals(note.id, 7376);
		Assert.assertEquals(note.x, 264);
		Assert.assertEquals(note.y, 185);
		Assert.assertEquals(note.width, 188);
		Assert.assertEquals(note.height, 185);
		Assert.assertEquals(note.post_id, 312859);
		Assert.assertEquals(note.body, "Uraraka Ochako\n(Boku no Hero Academia)");
		Assert.assertEquals(note.version, 1);
	}

	@Test()
	public void Parse_Wiki_Item_Correctly() throws IOException, JSONException {
		JSONObject object = loadJsonArray("wiki.json").getJSONObject(0);
		Wiki wiki = MetaController.convertjson2Wiki(object);
		Assert.assertNotNull(wiki);

		/*  */
		Assert.assertEquals(wiki.uid, 905);
		//Assert.assertEquals(wiki.created_at, 93238);
		Assert.assertEquals(wiki.updated_at, 0);
		Assert.assertEquals(wiki.title, "alice_in_musicland_(vocaloid)");
		Assert.assertEquals(wiki.updater_id, 96529);
		Assert.assertEquals(wiki.version, 1);
	}
}
