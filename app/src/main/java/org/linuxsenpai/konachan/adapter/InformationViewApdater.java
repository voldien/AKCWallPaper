package org.linuxsenpai.konachan.adapter;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.db.Post;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InformationViewApdater extends BaseAdapter {
	private final Post post;
	private final List<String> keys;
	private final Pattern pattern;

	public InformationViewApdater(Post post, List<String> keys) {
		this.post = post;
		this.keys = keys;
		final String urlRegPattern = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)";
		pattern = Pattern.compile(urlRegPattern);
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
		View view = LayoutInflater.from(parent.getContext()).
				inflate(R.layout.item_information_view, parent, false);
		TextView textViewKey = view.findViewById(R.id.textview);
		TextView textViewValue = view.findViewById(R.id.textviewvalue);

		String key = String.format("%s:", keys.get(position));
		String value;
		//TODO improve the logic for fetching the correct value.
		switch (position) {
			case 0:
				value = String.valueOf(post.uid);
				break;
			case 1:
				value = post.tags;
				break;
			case 2:
				value = post.author;
				break;
			case 3:
				value = post.source;
				break;
			default:
			case 4:
				value = post.sampleUrl;
				break;
		}

		textViewKey.setText(key);
		textViewValue.setText(value);

		/*  Determine if URL.   */
		Matcher m = pattern.matcher(value);
		boolean isUrl = m.matches();
		if (isUrl)
			Linkify.addLinks(textViewValue, Linkify.WEB_URLS);

		return view;
	}

	public static class InformationViewHolder extends RecyclerView.ViewHolder {
		private TextView textView;

		public InformationViewHolder(@NonNull View itemView) {
			super(itemView);
			//textView = (TextView)itemView.findViewById(R.id.textview);
		}

		public void clear() {
		}

/*		public void bindTo(History history) {
			textView.setText(history.name);
		}*/
	}
}
