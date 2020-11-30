package org.linuxsenpai.konachan.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.History;
import org.linuxsenpai.konachan.db.Note;
import org.linuxsenpai.konachan.db.Tag;
import org.linuxsenpai.konachan.db.Wiki;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.settings, new SettingsFragment())
				.commit();
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void clearCache(Context context) {
		AppDatabase database = AppDatabase.getAppDatabase(context);
		List<Tag> tags = AppDatabase.getAppDatabase(context).tagDao().getAll();
		List<Wiki> wikiList = AppDatabase.getAppDatabase(context).wikiDao().getAll();
		List<Note> noteList = AppDatabase.getAppDatabase(context).noteDao().getAll();
		List<History> historyList = AppDatabase.getAppDatabase(context).historyDao().getAll();

		for (Tag tag : tags) {
			database.tagDao().delete(tag);
		}
	}

	public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.root_preferences, rootKey);
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			/*  Perform action based on the setting that has changed. */
			//getPreferenceManager().findPreference("disk_")
			return false;
		}

	}
}
