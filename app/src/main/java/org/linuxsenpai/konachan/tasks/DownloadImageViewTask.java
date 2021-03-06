package org.linuxsenpai.konachan.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.R;

public class DownloadImageViewTask extends AsyncTask<String, Void, Bitmap> {
	final ImageView bmImage;

	public DownloadImageViewTask(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11;

		try {
			mIcon11 = Network.GetBitMap(urldisplay);
		} catch (Exception e) {
			mIcon11 = null;
			/*  TODO determine if additional error to handle.   */
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		/*  */
		if (result != null)
			bmImage.setImageBitmap(result);
		else
			bmImage.setImageResource(R.drawable.ic_get_app);
		this.bmImage.setVisibility(View.VISIBLE);
	}
}
