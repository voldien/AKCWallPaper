package org.linuxsenpai.konachan.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.linuxsenpai.konachan.Network;

import java.io.IOException;
import java.io.InputStream;

public class DownloadContentTask extends AsyncTask<String, Void, InputStream> {

	private final Context context;

	public DownloadContentTask(Context context) {
		this.context = context;
	}

	@Override
	protected InputStream doInBackground(String... strings) {
		try {
			return Network.GetContextHTTPStream(context, "");
		} catch (IOException e) {
			return null;
//			e.printStackTrace();
		}
	}
}
