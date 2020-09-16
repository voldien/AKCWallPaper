package org.linuxsenpai.konachan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Network {

	public static String GetDomain(@NonNull Context context) {
		SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);

		return preferenceManager.getString("konachan-domain", "konachan.net");
	}

	public static String GetFullDomainURL(@NonNull Context context) {
		SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);
		String domain = GetDomain(context);
		String protocol = "https";
		if (!preferenceManager.getBoolean("https", true))
			protocol = "http";


		return String.format("%s://%s", protocol, domain);
	}

	public static URLConnection GetContextConnection(Context context, String query) throws IOException {
		SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);

		String domain = GetDomain(context);
		String protocol = "https";
		if (!preferenceManager.getBoolean("https", true))
			protocol = "http";


		String url = String.format("%s://%s/%s", protocol, domain, query);
		return GetConnection(url);
	}

	public static URLConnection GetConnection(@NonNull String url) throws IOException {
		URL urlobj = new URL(url);
		return urlobj.openConnection();
	}

	public static InputStream GetContextHTTPStream(@NonNull Context context, @NonNull String urlPath) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) Network.GetContextConnection(context, urlPath);

		urlConnection.addRequestProperty("Cache-Control", "no-cache");
		urlConnection.addRequestProperty("Cache-Control", "max-age=0");

		InputStream in = urlConnection.getInputStream();
		int responseCode = urlConnection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed to connect");
		}
		return (FileInputStream) in;
	}

	public static InputStream GetHTTPStream(@NonNull String urlPath) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) GetConnection(urlPath);

		/*  */
		urlConnection.addRequestProperty("Cache-Control", "no-cache");
		urlConnection.addRequestProperty("Cache-Control", "max-age=0");

		InputStream in = urlConnection.getInputStream();
		int responseCode = urlConnection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed to connect");
		}
		return in;
	}

	public static Bitmap GetBitMap(@NonNull String url) {
		Bitmap mIcon11 = null;
		try {
			InputStream in = GetHTTPStream(url);
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon11;
	}

	public static JSONArray GetJsonObjectQuery(@NonNull Context context, @NonNull String query) {
		JSONArray mIcon11 = null;

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) GetContextConnection(context, query);
			InputStream in = urlConnection.getInputStream();


			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in));
			StringBuilder responseStrBuilder = new StringBuilder();
			String line = streamReader.readLine();
			do {
				responseStrBuilder.append(line);
				line = streamReader.readLine();
			} while (line != null);

			Log.d("DownloadJsonTask", streamReader.toString());

			mIcon11 = new JSONArray(responseStrBuilder.toString());


		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	public static JSONArray GetJsonObject(@NonNull String url) {
		JSONArray mIcon11 = null;
		//TODO encapsulate the download to stream via the preference manager.
		try {
			InputStream in = GetHTTPStream(url);

			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in));
			StringBuilder responseStrBuilder = new StringBuilder();
			String line = streamReader.readLine();
			do {
				responseStrBuilder.append(line);
				line = streamReader.readLine();
			} while (line != null);

			mIcon11 = new JSONArray(responseStrBuilder.toString());


		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon11;
	}

	public static boolean isNetworkAvailable(@NonNull Context context) {
		boolean available = false;
		/** Getting the system's connectivity service */
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		/** Getting active network interface  to get the network's status */
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable())
			available = true;

		/** Returning the status of the network */
		return available;
	}

}
