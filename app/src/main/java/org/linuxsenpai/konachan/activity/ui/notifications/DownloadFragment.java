package org.linuxsenpai.konachan.activity.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.linuxsenpai.konachan.R;

public class DownloadFragment extends Fragment {

	private DownloadViewModel downloadViewModel;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		downloadViewModel =
				ViewModelProviders.of(this).get(DownloadViewModel.class);
		View root = inflater.inflate(R.layout.fragment_notifications, container, false);
		final TextView textView = root.findViewById(R.id.text_notifications);
		downloadViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
			@Override
			public void onChanged(@Nullable String s) {
				textView.setText(s);
			}
		});
		return root;
	}
}
