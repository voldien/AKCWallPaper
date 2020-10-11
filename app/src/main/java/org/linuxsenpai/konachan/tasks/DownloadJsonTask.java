package org.linuxsenpai.konachan.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class DownloadJsonTask extends AsyncTask<String, Void, JSONArray> {

	public DownloadJsonTask() {

	}

	protected JSONArray doInBackground(String... urls) {
		JSONArray mIcon11 = null;
		//TODO encapsulate the download to stream via the preference manager.
		try {
			URL url = new URL(urls[0]);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.addRequestProperty("Cache-Control", "no-cache");
			urlConnection.addRequestProperty("Cache-Control", "max-age=0");

			InputStream in = urlConnection.getInputStream();
			int responseCode = urlConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(in));
				StringBuilder responseStrBuilder = new StringBuilder();
				String line = streamReader.readLine();
				do {
					responseStrBuilder.append(line);
					line = streamReader.readLine();
				} while (line != null);

				Log.d("DownloadJsonTask", streamReader.toString());

				mIcon11 = new JSONArray(responseStrBuilder.toString());

			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	//TODO make part of the application and reference it.
/*	public static String loadFileString(String localResource) throws IOException {

		InputStream inputStream;
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder responseStrBuilder = new StringBuilder();
		String line;
		line = streamReader.readLine();

		do {
			responseStrBuilder.append(line);
			line = streamReader.readLine();
		} while (line != null);

		return responseStrBuilder.toString();
	}*/

	protected void onPostExecute(JSONArray result) {
		/*  */
	}
}
