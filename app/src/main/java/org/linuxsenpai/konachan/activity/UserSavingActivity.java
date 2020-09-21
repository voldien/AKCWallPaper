package org.linuxsenpai.konachan.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.DemoCollectionPagerAdapter;

//TODO rename!
public class UserSavingActivity extends AppCompatActivity {

	final String[] tabLabels = {"History", "Favorite"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_saving);
		ViewPager2 viewPager = findViewById(R.id.pager);
		viewPager.setAdapter(new DemoCollectionPagerAdapter(this));

		TabLayout tabLayout = findViewById(R.id.tab_layout);
		new TabLayoutMediator(tabLayout, viewPager,
				(tab, position) -> tab.setText(tabLabels[position])
		).attach();
	}

}
