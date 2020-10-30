package org.linuxsenpai.konachan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.adapter.InformationViewApdater;
import org.linuxsenpai.konachan.db.Post;

import java.util.ArrayList;
import java.util.Arrays;


public class InformationFragment extends Fragment {

	private static final String ARG_POST_OBJECT = "post-object";
	private static final String ARG_POST_KEY_STRINGS = "post-object-key-array";
	InformationViewModel informationViewModel;
	private ListView listview;
	private ArrayList<String> keys;

	public InformationFragment() {

	}

	//TODO change so that it can take all type of API data types.
	public static InformationFragment newInstance(@NonNull Post post, @NonNull String[] keys) {
		InformationFragment fragment = new InformationFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_POST_OBJECT, post);
		ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(keys));
		args.putStringArrayList(ARG_POST_KEY_STRINGS, arrayList);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMenuVisibility(false);

		informationViewModel = new ViewModelProvider(this).get(InformationViewModel.class);

		informationViewModel.getInformation().observe(getViewLifecycleOwner(), new Observer<Post>() {
			@Override
			public void onChanged(Post post) {
				listview.setAdapter(new InformationViewApdater(post, keys));
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_information, container, false);
		this.listview = view.findViewById(R.id.listview);

		if (getArguments() != null) {
			Post post = getArguments().getParcelable(ARG_POST_OBJECT);
			this.keys = getArguments().getStringArrayList(ARG_POST_KEY_STRINGS);
			this.informationViewModel.loadInformation(post);
		}
		return view;
	}
}
