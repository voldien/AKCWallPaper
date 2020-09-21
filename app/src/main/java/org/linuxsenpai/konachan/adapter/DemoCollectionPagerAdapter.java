package org.linuxsenpai.konachan.adapter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.linuxsenpai.konachan.activity.ui.favorites.FavoriteFragment;
import org.linuxsenpai.konachan.activity.ui.history.HistoryFragment;

//TODO rename.
public class DemoCollectionPagerAdapter extends FragmentStateAdapter {
	public DemoCollectionPagerAdapter(FragmentActivity fm) {
		super(fm);
	}

/*	@Override
	public Fragment getItem(int i) {

	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}*/

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new HistoryFragment();
				break;
			case 1:
				fragment = new FavoriteFragment();
				break;
			default:
				break;
		}
		Bundle args = new Bundle();
		// Our object is just an integer :-P
		//args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getItemCount() {
		return 2;
	}
}
