package org.linuxsenpai.konachan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Post;

import java.util.List;

public class InformationViewApdater extends BaseAdapter {
	private Post post;
	private List<String> keys;

	public InformationViewApdater(Post post, List<String> keys) {
		this.post = post;
		this.keys = keys;
	}


	@Override
	public int getCount() {
		return keys.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) LayoutInflater.from(parent.getContext()).
				inflate(R.layout.item_information_view, parent, false);

		String key = keys.get(position);
		/*  TODO add Get type.   */
		textView.setText(key);
		return textView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

}
