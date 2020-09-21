package org.linuxsenpai.konachan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.linuxsenpai.konachan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DummyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DummyFragment extends Fragment {
	public DummyFragment() {
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment Dummy.
	 */
	public static DummyFragment newInstance() {
		DummyFragment fragment = new DummyFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_dummy, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBackground));
		return view;
	}
}
