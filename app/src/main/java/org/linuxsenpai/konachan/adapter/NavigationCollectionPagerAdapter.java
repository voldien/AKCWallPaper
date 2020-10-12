package org.linuxsenpai.konachan.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.linuxsenpai.konachan.activity.ui.favorites.FavoriteFragment;
import org.linuxsenpai.konachan.activity.ui.history.HistoryFragment;
import org.linuxsenpai.konachan.fragment.PostRecycleImageFragment;
import org.linuxsenpai.konachan.fragment.TagListFragment;
import org.linuxsenpai.konachan.fragment.WikiListFragment;

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
				fragment = PostRecycleImageFragment.newInstance("", 1, null);
				break;
			case 1:
				fragment = TagListFragment.newInstance(1);
				break;
			case 2:
				fragment = WikiListFragment.newInstance("", "");
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
		return fragment;
	}

	@Override
	public int getItemCount() {
		return TAB_LABELS.length;
	}
}
