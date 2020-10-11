package org.linuxsenpai.konachan.events;


import android.view.View;
import android.widget.ImageView;

public class ImageLongClick implements View.OnLongClickListener {

	@Override
	public boolean onLongClick(View v) {
		ImageView imageView = (ImageView) v;
		imageView.showContextMenu();

		return false;
	}
}
