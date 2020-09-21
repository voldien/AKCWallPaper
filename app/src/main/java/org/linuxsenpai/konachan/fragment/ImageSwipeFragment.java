package org.linuxsenpai.konachan.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.view.ZoomableImageView;

import java.util.List;
import java.util.Map;


public class ImageSwipeFragment extends Fragment {
	public ImageSwitcher imageSwitcher;
	private ImageSwipeViewModel mViewModel;
	private Drawable preview;

	public static ImageSwipeFragment newInstance(@Nullable Drawable preview) {
		ImageSwipeFragment imageSwipeFragment = new ImageSwipeFragment();
		imageSwipeFragment.preview = preview;
		return imageSwipeFragment;
	}

	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_swipe, container, false);

		imageSwitcher = (ImageSwitcher) view.findViewById(R.id.image_switch);
		imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
			public View makeView() {
				final ImageView imageView = new ZoomableImageView(getContext(), null);
				imageView.setTransitionName("profile");
				registerForContextMenu(imageView);
				imageView.setOnClickListener(null);
				return imageView;
			}
		});

		/*  */
		ImageView imageView = (ImageView) imageSwitcher.getCurrentView();
		imageView.setImageDrawable(preview);
		String[] projection = {MediaStore.Images.Thumbnails._ID};
		//cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Thumbnails._ID + "");
		//columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

		Animation in = AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_in_left);
		imageSwitcher.setInAnimation(in);
		Animation out = AnimationUtils.loadAnimation(this.getContext(), android.R.anim.slide_out_right);
		imageSwitcher.setOutAnimation(out);

		prepareSharedElementTransition();

		// Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
		if (savedInstanceState == null) {
			postponeEnterTransition();
		}

		return view;
	}

	/**
	 * Prepares the shared element transition from and back to the grid fragment.
	 */
	private void prepareSharedElementTransition() {
		Transition transition =
				TransitionInflater.from(getContext())
						.inflateTransition(R.transition.change_image_transform);
		setSharedElementEnterTransition(transition);

		// A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
		setEnterSharedElementCallback(
				new SharedElementCallback() {
					@Override
					public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
						// Locate the image view at the primary fragment (the ImageFragment that is currently
						// visible). To locate the fragment, call instantiateItem with the selection position.
						// At this stage, the method will simply return the fragment at the position and will
						// not create a new one.

						View view = getCurrentImageView();
						if (view == null) {
							return;
						}
						// Map the first shared element name to the child ImageView.
						sharedElements.put(names.get(0), view);
					}
				});
	}

	public View getCurrentImageView() {
		return imageSwitcher.getCurrentView();
	}


	@Override
	public void onStart() {
		super.onStart();
		SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swiperefresh);
		if (swipeRefreshLayout != null)
			swipeRefreshLayout.setEnabled(false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(ImageSwipeViewModel.class);
	}

}
