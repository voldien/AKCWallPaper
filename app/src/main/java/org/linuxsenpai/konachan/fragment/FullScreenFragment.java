package org.linuxsenpai.konachan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;

import org.linuxsenpai.konachan.R;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullScreenFragment extends Fragment {

	private ImageView imageView;

	public FullScreenFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment FullScreenFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FullScreenFragment newInstance() {
		FullScreenFragment fragment = new FullScreenFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});*/
		//getParentFragment().startPostponedEnterTransition();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_full_screen, container, false);
		imageView = view.findViewById(R.id.image);
		imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				swapFullScreenImage();
				return false;
			}
		});

/*		Transition transition =
				TransitionInflater.from(getContext())
						.inflateTransition(R.transition.image_shared_element_transition);
		setSharedElementEnterTransition(transition);*/

		//setFullScreenImage();
		setEnterSharedElementCallback(
				new SharedElementCallback() {
					@Override
					public void onMapSharedElements(
							List<String> names, Map<String, View> sharedElements) {
						// Locate the image view at the primary fragment (the ImageFragment
						// that is currently visible). To locate the fragment, call
						// instantiateItem with the selection position.
						// At this stage, the method will simply return the fragment at the
						// position and will not create a new one.
						View view = getView();
						if (view == null) {
							return;
						}

						// Map the first shared element name to the child ImageView.
						sharedElements.put(names.get(0), view.findViewById(R.id.image));
					}
				});
		return view;
	}

	public void swapFullScreenImage() {
		boolean fullScreen = (getView().getWindowSystemUiVisibility() & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
		if(!fullScreen) {
			imageView.setSystemUiVisibility(
					imageView.SYSTEM_UI_FLAG_IMMERSIVE
							// Set the content to appear under the system bars so that the
							// content doesn't resize when the system bars hide and show.
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							// Hide the nav bar and status bar
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN);
		}else{

		}
	}
}
