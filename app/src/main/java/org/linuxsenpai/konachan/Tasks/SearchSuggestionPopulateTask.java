package org.linuxsenpai.konachan.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.adapter.SearchSuggestionAdapter;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Tag;

public class SearchSuggestionPopulateTask extends AsyncTask<String, Void, JSONArray> {

	private SearchSuggestionAdapter adapter;
	private Context context;
	private SharedPreferences settings;
	private int numEntries;

	public SearchSuggestionPopulateTask(Context context, SearchSuggestionAdapter mSearchSuggestionAdapater, int numEntries) {
		this.adapter = mSearchSuggestionAdapater;
		this.context = context;
		this.numEntries = numEntries;
	}

	@Override
	protected JSONArray doInBackground(String... queryTexts) {
		String url = String.format("tag.json?name=%s*&limit=5&page=1&order=count", queryTexts[0]);
		//Log.d("SearchSuggestionPopulateTask", url);
		return Network.GetJsonObjectQuery(this.context, url);
	}

	@Override
	protected void onPostExecute(JSONArray result) {
		super.onPostExecute(result);

		if (result != null) {
			final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "name", "count", "id", "type"});

			for (int i = 0; i < result.length(); i++) {

				JSONObject jsonObject;
				try {
					jsonObject = result.getJSONObject(i);
					Tag tag = MetaController.convertJson2Tag(jsonObject);
					//TODO add condition.
					c.addRow(new Object[]{i, tag.name, tag.count, tag.uid, tag.type});
					adapter.changeCursor(c);
				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		}
	}

}
