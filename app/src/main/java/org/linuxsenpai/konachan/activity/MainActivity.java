package org.linuxsenpai.konachan.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.fragment.PostRecycleImageFragment;
import org.linuxsenpai.konachan.preference.SharedPreference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	public static final String KEY_SEARCH_QUERY = "query";

	private static final String TAG = "MainActivity";
	public MutableLiveData<SearchView> searchView;
	public SwipeRefreshLayout swipeRefreshLayout;
	public SharedPreference sharedPreference;
	//TODO determine how to progate the information.
	BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = manager.getActiveNetworkInfo();
			//doSomethingOnNetworkChange(ni);
		}
	};
/*	private ActivityResultLauncher<String> requestPermissionLauncher =
			registerForActivityResult(new RequestPermission(), isGranted -> {
				if (isGranted) {
					// Permission is granted. Continue the action or workflow in your
					// app.
				} else {
					// Explain to the user that the feature is unavailable because the
					// features requires a permission that the user has denied. At the
					// same time, respect the user's decision. Don't link to system
					// settings in an effort to convince the user to change their
					// decision.
				}
			});*/


	private AppBarConfiguration mAppBarConfiguration;

	public LiveData<SearchView> getSearchView() {
		return searchView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrolling);
		MetaController.getInstance(this);
		searchView = new MutableLiveData<>();

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/*  */
		ValidateStatusOfApp();

		initHttpCache();

		/*  Create main fragment.   */
		//TODO determine if need to be relocated based on the savedInstance.

		//TODO make a intent for a sync call.
		if (savedInstanceState == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
			transaction.replace(R.id.main_fragment, PostRecycleImageFragment.newInstance("", 1, null));
			transaction.addToBackStack("PostRecycleFragment");
			transaction.commit();/**/

//			(viewGroup.getMeasuredHeight()/3) - (marginTop + marginBot)
			SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
			Intent intent = new Intent(Intent.ACTION_SEARCH);
			intent.putExtra(SearchManager.QUERY, "");

			// Get the intent, verify the action and get the query
			handleIntent(getIntent());
		} else {
			/*  Revert to the previous state of the the activity.    */
			String query = savedInstanceState.getString(KEY_SEARCH_QUERY).toString();
			//this.searchView.setQuery(query, true);
		}

	}

	private void ValidateStatusOfApp() {
		/*  */
		String[] request_codes = new String[]{Manifest.permission.INTERNET, Manifest.permission.MEDIA_CONTENT_CONTROL,
				Manifest.permission.SET_WALLPAPER, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_NETWORK_STATE};
		ActivityCompat.requestPermissions(this, request_codes, 0);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			// Do something for lollipop and above versions
		} else {
			// do something for phones running an SDK before lollipop
		}
	}

	private void initHttpCache() {
		/*  Initialize the cache directory.  */
		try {
			File httpCacheDir = new File(this.getCacheDir(), "http");
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				HttpResponseCache.install(httpCacheDir, httpCacheSize);
			}
			{
				Class.forName("android.net.http.HttpResponseCache")
						.getMethod("install", File.class, long.class)
						.invoke(null, httpCacheDir, httpCacheSize);
			}
		} catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
			Log.i(TAG, "HTTP response cache installation failed:" + e);
			/*  */
			releaseHttpCache();
			View parentLayout = findViewById(android.R.id.content);
			Snackbar.make(parentLayout, getString(R.string.text_error_create_http_cache), Snackbar.LENGTH_SHORT).show();
		} catch (IllegalAccessException | InvocationTargetException e) {
			releaseHttpCache();
			View parentLayout = findViewById(android.R.id.content);
			Snackbar.make(parentLayout, getString(R.string.text_error_create_http_cache), Snackbar.LENGTH_SHORT).show();
		}
	}

	private void releaseHttpCache() {
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttachFragment(@NonNull Fragment fragment) {
		super.onAttachFragment(fragment);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		unregisterReceiver(networkStateReceiver);
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		releaseHttpCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onSearchRequested(@Nullable SearchEvent searchEvent) {
		return super.onSearchRequested(searchEvent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case 0:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 &&
						grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission is granted. Continue the action or workflow
					// in your app.
				} else {
					// Explain to the user that the feature is unavailable because
					// the features requires a permission that the user has denied.
					// At the same time, respect the user's decision. Don't link to
					// system settings in an effort to convince the user to change
					// their decision.
				}
				return;
		}
		// Other 'case' lines to check for other
		// permissions this app might request.
	}

	protected void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			List<Fragment> fragments = getSupportFragmentManager().getFragments();
			if (fragments.size() > 1) {
				/*  Clear all prior fragment.   */
				for (Fragment fragment : getSupportFragmentManager().getFragments()) {
					getSupportFragmentManager().beginTransaction().remove(fragment).commit();

				}
			} else {
				if (fragments.get(0).getTag() != "") {

				}
			}

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
			transaction.replace(R.id.main_fragment, PostRecycleImageFragment.newInstance(query, 1, null));
			transaction.addToBackStack(null);
			transaction.commit();/**/
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
				|| super.onSupportNavigateUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		//TODO rename layout name.
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_recycle_image_view_options, menu);

		// Associate searchable configuration with the SearchView
		this.searchView.setValue((SearchView) menu.findItem(R.id.action_searchview).getActionView());
		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		this.searchView.getValue().setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem searchView = menu.findItem(R.id.action_searchview);
		if (searchView != null)
			this.searchView.setValue((SearchView) menu.findItem(R.id.action_searchview).getActionView());
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_settings:
				DisplaySetting();
				return true;
			case R.id.action_wikipage:
				DisplayWikiSearchFragment();
				return true;
			case R.id.action_tagpage:
				DisplayTagSearchFragment();
				return true;
			case R.id.action_saveuser:
				DisplayUserSavedData();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void DisplaySetting() {
		Intent settingIntent = new Intent(this, SettingsActivity.class);
		this.startActivity(settingIntent);
	}

	private void DisplayWikiSearchFragment() {

	}

	private void DisplayTagSearchFragment() {

	}

	private void DisplayUserSavedData() {
		Intent user_preference = new Intent(this, UserSavingActivity.class);
		this.startActivity(user_preference);
	}
}
