package org.linuxsenpai.konachan.app;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import org.linuxsenpai.konachan.BuildConfig;
import org.linuxsenpai.konachan.db.AppDatabase;


public class KonaChanApp extends Application {

	public static void allowDiskReads(Runnable runnable) {
		StrictMode.ThreadPolicy oldThreadPolicy;
		oldThreadPolicy = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(oldThreadPolicy).permitDiskReads().build());
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG) {
			StrictMode.setThreadPolicy(
					new StrictMode.ThreadPolicy.Builder()
							.detectAll()
							.penaltyLog()
							.build());

			StrictMode.setVmPolicy(
					new StrictMode.VmPolicy.Builder()
							.detectLeakedSqlLiteObjects()
							.penaltyLog()
							.penaltyDeath()
							.build());


			/*  Debugging information.  */
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			int version = Build.VERSION.SDK_INT;
			String versionRelease = Build.VERSION.RELEASE;

			/*	Debug information.*/
			Log.e("KonaChan", "manufacturer " + manufacturer
					+ " \n model " + model
					+ " \n version " + version
					+ " \n versionRelease " + versionRelease
			);

		}
		/*  Init database.  */
		AppDatabase.getAppDatabase(this.getBaseContext());
	}
}
