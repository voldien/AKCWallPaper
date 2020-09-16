package org.linuxsenpai.konachan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Responsible for creating an
 * activity for which displays the
 * splash screen of the program while
 * loading the resources in the background.
 */
public class SplashActivity extends Activity {

	static public final String TAG = "Splash";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*  Create main activity.   */
		Log.d(TAG, "Creating Activity intent.");
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Log.d(TAG, "Finishing Splash.");

		/*  Close activity. */
		finish();
	}
}
