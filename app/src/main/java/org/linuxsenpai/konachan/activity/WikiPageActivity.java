package org.linuxsenpai.konachan.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.linuxsenpai.konachan.R;

public class WikiPageActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_wiki_page);

		Bundle bundle = getIntent().getExtras();
		bundle.getParcelable("");
	}
}
