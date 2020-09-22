package org.linuxsenpai.konachan.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionInflater;

import org.linuxsenpai.konachan.Events.SearchQueryListener;
import org.linuxsenpai.konachan.Events.SearchQuerySuggestionListener;
import org.linuxsenpai.konachan.ItemOffsetDecoration;
import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.activity.MainActivity;
import org.linuxsenpai.konachan.adapter.PostImagesAdapter;
import org.linuxsenpai.konachan.adapter.SearchSuggestionAdapter;
import org.linuxsenpai.konachan.api.MetaController;

import java.util.List;
import java.util.Map;

public class PostRecycleImageFragment extends Fragment {

	public static final String KEY_RECYCLE_POSITION = "position";
	public static final String KEY_RECYCLE_TAG = "tag";
	private static final String ARG_COLUMN_COUNT = "column-count";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String SOURCE = "source";
	private static final String ARG_QUERY_STRING = "param1";
	private static final String KEY_RECYCLE_VIEW_COLUMN = "column";

	private RecycleImageViewModel viewModel;
	private RecyclerView recyclerView;
	// TODO: Rename and change types of parameters
	private SearchSuggestionAdapter SearchAdapter;
	//	private SearchView searchView;
	private PostImagesAdapter postImagesAdapter;
	private InputMethodManager imm;
//	private RectF mCurrentViewport =
//			new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);
//	private Rect mContentRect;
//	private ScaleGestureDetector mScaleGestureDetector;
//
//
//	/**
//	 * The scale listener, used for handling multi-finger scale gestures.
//	 */
//	protected final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
//			= new ScaleGestureDetector.SimpleOnScaleGestureListener() {
//		/**
//		 * This is the active focal point in terms of the viewport. Could be a local
//		 * variable but kept here to minimize per-frame allocations.
//		 */
//		private PointF viewportFocus = new PointF();
//		private float lastSpanX;
//		private float lastSpanY;
//
//		// Detects that new pointers are going down.
//		@Override
//		public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
//			lastSpanX = ScaleGestureDetectorCompat.
//					getCurrentSpanX(scaleGestureDetector);
//			lastSpanY = ScaleGestureDetectorCompat.
//					getCurrentSpanY(scaleGestureDetector);
//			return true;
//		}
//
//		@Override
//		public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//
//			float spanX = ScaleGestureDetectorCompat.
//					getCurrentSpanX(scaleGestureDetector);
//			float spanY = ScaleGestureDetectorCompat.
//					getCurrentSpanY(scaleGestureDetector);
//
//			float newWidth = lastSpanX / spanX * mCurrentViewport.width();
//			float newHeight = lastSpanY / spanY * mCurrentViewport.height();
//
//			float focusX = scaleGestureDetector.getFocusX();
//			float focusY = scaleGestureDetector.getFocusY();
//			// Makes sure that the chart point is within the chart region.
//			// See the sample for the implementation of hitTest().
//			hitTest(scaleGestureDetector.getFocusX(),
//					scaleGestureDetector.getFocusY(),
//					viewportFocus);
//
//			mCurrentViewport.set(
//					viewportFocus.x
//							- newWidth * (focusX - mContentRect.left)
//							/ mContentRect.width(),
//					viewportFocus.y
//							- newHeight * (mContentRect.bottom - focusY)
//							/ mContentRect.height(),
//					0,
//					0);
//			mCurrentViewport.right = mCurrentViewport.left + newWidth;
//			mCurrentViewport.bottom = mCurrentViewport.top + newHeight;
//			// ...
//			// Invalidates the View to update the display.
//			//ViewCompat.postInvalidateOnAnimation(InteractiveLineGraphView.this);
//
//			lastSpanX = spanX;
//			lastSpanY = spanY;
//			return true;
//		}
//	};

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

	public PostRecycleImageFragment() {
		// Required empty public constructor
	}

	public static PostRecycleImageFragment newInstance(String query, int columnCount, MetaController metaController) {
		PostRecycleImageFragment fragment = new PostRecycleImageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_QUERY_STRING, query);
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		/*  */
		SearchAdapter = new SearchSuggestionAdapter(this.getActivity(), null, false);


		getView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				boolean retVal = mScaleGestureDetector.onTouchEvent(event);
//				retVal = mGestureDetector.onTouchEvent(event) || retVal;
//				return retVal;
				return true;
			}
		});

		// Associate searchable configuration with the SearchView
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.getSearchView().observe(getViewLifecycleOwner(), new Observer<SearchView>() {
			@Override
			public void onChanged(SearchView searchView) {
				searchView.setSuggestionsAdapter(SearchAdapter);
				searchView.setOnSuggestionListener(new SearchQuerySuggestionListener(searchView, SearchAdapter));
				searchView.setOnQueryTextListener(new SearchQueryListener(getContext(), searchView, SearchAdapter, viewModel));
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		LinearLayoutManager layoutManager = null;
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			layoutManager = new GridLayoutManager(this.getContext(), 2);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
		} else {
			//TODO resolve
		}
		/*  update*/
		this.recyclerView.setLayoutManager(layoutManager);
	}

	private void updateRecycleLayout(int orientation) {
		// Checks the orientation of the screen
		LinearLayoutManager layoutManager = null;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			layoutManager = new GridLayoutManager(this.getContext(), 2);
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
		} else {
			//TODO resolve
		}
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());

		if (setting.getInt("layout", 0) == 0) {
			layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
			DisplayMetrics displayMetrics = new DisplayMetrics();
			this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int height = displayMetrics.heightPixels;
			int width = displayMetrics.widthPixels;
		} else {
			/*  TODO change the span count.*/
			layoutManager = new GridLayoutManager(this.getContext(), 3);
		}
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(layoutManager);

		/*  */
		int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.photos_list_spacing);
		recyclerView.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*  Create model.   */
		viewModel = new ViewModelProvider(this).get(RecycleImageViewModel.class);
		viewModel.getSearchQuery().observe(this, new Observer<String>() {
			@Override
			public void onChanged(String searchQuery) {
				//TODO add support for compute diff.
				if (recyclerView.getAdapter() == null) {
					postImagesAdapter = new PostImagesAdapter(searchQuery);
					recyclerView.setAdapter(postImagesAdapter);

				} else {
					/*  */
					postImagesAdapter.setPostTag(searchQuery);
				}
				recyclerView.getLayoutManager().scrollToPosition(0);
			}
		});

		if (savedInstanceState == null) {
			if (getArguments() != null) {
				String query = getArguments().getString(ARG_QUERY_STRING);
				this.viewModel.search(query);
			}

		} else {
			/*  Revert to the previous state of the the activity.    */
			int position = savedInstanceState.getInt(KEY_RECYCLE_POSITION);
			String query = savedInstanceState.getString(KEY_RECYCLE_TAG);
			this.viewModel.search(query);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recycle_image, container, false);
		recyclerView = view.findViewById(R.id.recycler_view);

		//TODO improve performance
		imm = (InputMethodManager)
				getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					if (imm.isActive()) {
						View view = getActivity().getCurrentFocus();
						if (view != null)
							imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
				}
				if (newState == RecyclerView.SCROLL_INDICATOR_TOP) {
					if (!imm.isActive()) {  //TODO add condition if the search view is active.
						View view = getActivity().getCurrentFocus();
//						imm.showSoftInput(view.getWindowToken(), 0);
					}
				}
			}
		});

		/*  Get layout. */
		updateRecycleLayout(getActivity().getResources().getConfiguration().orientation);

		SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swiperefresh);
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					//TODO change the search to a single method.
					MainActivity mainActivity = (MainActivity) getActivity();
					if (mainActivity != null) {
						SearchView searchView = mainActivity.getSearchView().getValue();
						viewModel.search(searchView.getQuery().toString());
					}
				}
			});
		}

		prepareTransitions();
		postponeEnterTransition();

		return view;
	}

	private void prepareTransitions() {
		setExitTransition(TransitionInflater.from(getContext())
				.inflateTransition(R.transition.change_image_transform));

		// A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
		setExitSharedElementCallback(
				new SharedElementCallback() {
					@Override
					public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
						// Locate the ViewHolder for the clicked position.
						RecyclerView.ViewHolder selectedViewHolder = recyclerView
								.findViewHolderForAdapterPosition(0);
						if (selectedViewHolder == null) {
							return;
						}
						// Map the first shared element name to the child ImageView.
						sharedElements
								.put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.item_image_view_image));
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		/*  Disable if in view. */
		SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swiperefresh);
		if (swipeRefreshLayout != null)
			swipeRefreshLayout.setEnabled(true);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		/*  Save the current state of the view. */
		outState.putInt(KEY_RECYCLE_POSITION, this.recyclerView.getBottom());
		outState.putInt(KEY_RECYCLE_VIEW_COLUMN, this.recyclerView.getLayoutManager().getBaseline());
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}

	private class ScaleListener
			extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

			//invalidate();
			return true;
		}
	}
}
