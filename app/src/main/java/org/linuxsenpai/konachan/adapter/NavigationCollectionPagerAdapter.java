package org.linuxsenpai.konachan.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.linuxsenpai.konachan.fragment.PostImagesFragment;
import org.linuxsenpai.konachan.fragment.TagListFragment;
import org.linuxsenpai.konachan.fragment.WikiListFragment;
import org.linuxsenpai.konachan.fragment.favorites.FavoriteFragment;
import org.linuxsenpai.konachan.fragment.history.HistoryFragment;

import java.util.Objects;

import static org.linuxsenpai.konachan.activity.MainActivity.TAB_LABELS;

public class NavigationCollectionPagerAdapter extends FragmentStateAdapter {
	public NavigationCollectionPagerAdapter(FragmentActivity fm) {
		super(fm);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = PostImagesFragment.newInstance("", 1);
				break;
			case 1:
				fragment = TagListFragment.newInstance(1, null);
				break;
			case 2:
				fragment = WikiListFragment.newInstance(1);
				break;
			case 3:
				fragment = FavoriteFragment.newInstance();
				break;
			case 4:
				fragment = HistoryFragment.newInstance();
				break;
			default:
				break;
		}
		return Objects.requireNonNull(fragment);
	}

	@Override
	public int getItemCount() {
		return TAB_LABELS.length;
	}
}
