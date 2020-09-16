package org.linuxsenpai.konachan.Events;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.fragment.SingleViewFragment;

public class SingleViewImageSwipeOnTouch implements View.OnTouchListener {

	ImageSwitcher imageSwitcher;
	SingleViewFragment singleViewFragment;
	int current_position;
	//SwipeGestureDetector swipeGestureDetector;
	float initialX;

	public SingleViewImageSwipeOnTouch(SingleViewFragment singleViewFragment, ImageSwitcher switcher) {
		this.singleViewFragment = singleViewFragment;
		this.imageSwitcher = switcher;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//swipeGestureDetector.onT

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				initialX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				float finalX = event.getX();
				if (initialX > finalX) {
					//cursor.moveToPosition(position);
					//int imageID = cursor.getInt(columnIndex);
					//images.setImageURI(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
					//images.setBackgroundResource(R.drawable.mb__messagebar_divider);

					imageSwitcher.showNext();
					Toast.makeText(v.getContext(), "Next Image",
							Toast.LENGTH_LONG).show();
					//position++;
				} else {
					//	if(position > 0)
					//	{
					//	cursor.moveToPosition(position);
					//	int imageID = cursor.getInt(columnIndex);
					//DownloadImageViewTask downloadImageViewTask = new DownloadImageViewTask((ImageView)imageSwitcher.getCurrentView());
					ImageView im = (ImageView) imageSwitcher.getNextView();
					im.setImageResource(R.drawable.e4ecf82091e74124117661b645ece376);
					//images.setImageURI(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
					//images.setBackgroundResource(R.drawable.ic_launcher);
					Toast.makeText(v.getContext(), "previous Image",
							Toast.LENGTH_LONG).show();
					imageSwitcher.showPrevious();
					//	position= position-1;
					Toast.makeText(v.getContext(), "No More Images To Swipe",
							Toast.LENGTH_LONG).show();

				}
		}

		return true;
	}
}
