package org.linuxsenpai.konachan;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linuxsenpai.konachan.api.Cursor;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.Post;

import java.io.IOException;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

		assertEquals("org.linuxsenpai.konachan", appContext.getPackageName());
	}

	@Test
	public void Preference_Use_Http_Request() {
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		//URLConnection connection = Network.GetContextConnection(appContext, "");
		//connection instanceof
		//connection.
	}

	public void Preference_Use_Https_Request() {

	}

	@Test
	public void API_LoadCursor() {
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
		assertNotNull(appDatabase);

		MetaController metaController = MetaController.getInstance(appContext);
		assertNotNull(metaController);
		Cursor<Post> postCursor = metaController.getPostItems("cat");
		assertNotNull(postCursor);

		Post post = null;
		try {
			post = postCursor.next();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		assertNotNull(post);
		assertTrue(post.tags.contains("cat"));
		assertFalse(postCursor.hasPrevious());
		assertTrue(postCursor.hasNext());
	}

	@Test
	public void Preference_variable_state() {

	}


	@Test
	public void MetaController_Load_PostData() {

	}


	@Test(expected = Test.None.class)
	public void NetworkCreateConnection_NoThrow() throws IOException {
		String url = "https://konachan.net/post.json?tags=";
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

		/*  Create network connection.  */
		URLConnection connection = Network.GetConnection(url);
		Assert.assertNotNull(connection);
		connection.connect();
	}

	@Test
	public void NetworkJsonDownload() {
		String url = "https://konachan.net/post.json?tags=";

		/*  Download json object.   */
		JSONArray jsonObject = Network.GetJsonObject(url);
		Assert.assertNotNull(jsonObject);
		Assert.assertNotEquals(jsonObject.length(), 0);
	}
}
