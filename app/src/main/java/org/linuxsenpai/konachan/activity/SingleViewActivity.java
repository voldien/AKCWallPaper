package org.linuxsenpai.konachan.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.WallPaperUtil;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.fragment.InformationFragment;
import org.linuxsenpai.konachan.fragment.SingleViewViewModel;
import org.linuxsenpai.konachan.fragment.TagListFragment;
import org.linuxsenpai.konachan.preference.SharedPreference;
import org.linuxsenpai.konachan.tasks.DownloadImageViewTask;
import org.linuxsenpai.konachan.tasks.SetTagListTask;
import org.linuxsenpai.konachan.view.ZoomableImageView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SingleViewActivity extends AppCompatActivity {
	public static final String ARG_POST_OBJECT = "post-item-object";
	private static final String[] POST_KEYS = {"id", "tags", "created-date", "author", "source", "score", "file size", "file url", "preview url"};
	private static final String TAG = "";
	public ImageSwitcher imageSwitcher;
	DownloadImageViewTask downloadImageViewTask = null;
	SetTagListTask setTagListTask = null;
	private SingleViewViewModel mViewModel;
	private TagListFragment tagListFragment;
	private MenuItem favoriteMenu;
	private Toolbar toolbar;
	private SharedPreference sharedPreference;
	private boolean isImageFitToScreen = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportPostponeEnterTransition();
		setContentView(R.layout.fragment_single_view);
		sharedPreference = new SharedPreference();

		/*  */
		this.toolbar = findViewById(R.id.toolbar);
		this.imageSwitcher = findViewById(R.id.image_switcher);
		setSupportActionBar(toolbar);

		this.findViewById(R.id.image_thumbnail).setVisibility(View.GONE);

		this.imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
			public View makeView() {
				final ImageView imageView = new ZoomableImageView(getBaseContext(), null);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					imageView.setTransitionName("THUMBNAIL_IMAGE");
				}
				//TODO add with and height
				registerForContextMenu(imageView);
				imageView.setOnClickListener(null);
				imageView.setVisibility(View.VISIBLE);
				return imageView;
			}
		});
		this.imageSwitcher.setVisibility(View.VISIBLE);

		/*  TODO resolve animation and layout.  */
		FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					finishAfterTransition();
				else
					finish();
			}
		});

		/*  TODO add scroll animation transition effects.   */
		ScrollView scrollView = findViewById(R.id.scroll_view);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
				@Override
				public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

				}
			});
		}

		this.imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
			int current_position;
			//SwipeGestureDetector swipeGestureDetector;
			float initialX;

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
							im.setImageResource(R.drawable.ic_get_app);
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
		});

		setEnterSharedElementCallback(new SharedElementCallback() {
			@Override
			public void onRejectSharedElements(List<View> rejectedSharedElements) {
				super.onRejectSharedElements(rejectedSharedElements);
			}

			@Override
			public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
				super.onMapSharedElements(names, sharedElements);
			}
		});

		/*  Add the tag list fragment.  */
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
		this.tagListFragment = TagListFragment.newInstance(1, null);
		transaction.replace(R.id.single_view_fragment_information_container, this.tagListFragment);
		transaction.commit();

		/*  */
		this.mViewModel = new ViewModelProvider(this).get(SingleViewViewModel.class);
		this.mViewModel.getPost().observe(this, new Observer<Post>() {
			@Override
			public void onChanged(Post post) {
				displayPostItem(post);
				/*  Start getting tag information.  */
			}
		});


		/*  Load data from bundle or from saved state.  */
		if (savedInstanceState != null) {
			Post post = savedInstanceState.getParcelable(ARG_POST_OBJECT);
			if (post != null) {
				this.mViewModel.setPostItem(post);
			}
		} else {
			Bundle state = getIntent().getExtras();
			if (state != null) {
				Post post = state.getParcelable(ARG_POST_OBJECT);
				if (post != null) {
					this.mViewModel.setPostItem(post);
				}
			}
		}

		supportStartPostponedEnterTransition();
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(ARG_POST_OBJECT, this.mViewModel.getPost().getValue());
	}

	@Override
	public void onAttachFragment(@NonNull Fragment childFragment) {
		super.onAttachFragment(childFragment);
	}

	public void displayPostItem(final Post post) {

		/*  Check and update parent reference view. */
		//TODO improve
		if (post.parent_id > 0) {
			List<Post> parentList = AppDatabase.getAppDatabase(getApplicationContext()).postDao().loadAllByIds(new int[]{post.parent_id});
			if (parentList.size() > 0) {
				TextView textView = findViewById(R.id.textview_child_link);
				textView.setVisibility(View.VISIBLE);
				textView.setOnClickListener(new View.OnClickListener() {
					final private Post _post = post;

					@Override
					public void onClick(View v) {
						List<Post> postList = AppDatabase.getAppDatabase(getApplicationContext()).postDao().loadAllByIds(new int[]{_post.parent_id});
						if (postList.size() > 0) {
							imageSwitcher.getNextView();
							displayPostItem(postList.get(0));
						}
					}
				});
			}
		} else {
			/*  */
			TextView textView = findViewById(R.id.textview_child_link);
			textView.setVisibility(View.GONE);
		}

		this.imageSwitcher.getCurrentView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isImageFitToScreen = !isImageFitToScreen;
				setImageFullScreen(isImageFitToScreen);

			}
		});

		/*  Set if bookmarked as favorite.  */
		if (favoriteMenu != null) {
			SharedPreference sharedPreference = new SharedPreference();
			if (sharedPreference.isPostFavorite(this.getApplicationContext(), post)) {
				favoriteMenu.setIcon(R.drawable.ic_turned_in_black_24dp);
			} else {
				favoriteMenu.setIcon(R.drawable.ic_turned_in_not_black_24dp);
			}
		}

		/*  Download the image. */
		downloadImageViewTask = new DownloadImageViewTask((ImageView) imageSwitcher.getCurrentView());
		downloadImageViewTask.execute(post.sampleUrl);

		/*  Download and fetch the tags.    */
		setTagListTask = new SetTagListTask(tagListFragment);
		setTagListTask.execute(post);

		//TODO resolve later.
		setImageFullScreen(true);
		/*  TODO add notes. */
	}

	@Override
	public void onPause() {
		super.onPause();
		if (setTagListTask != null)
			setTagListTask.cancel(true);
		if (downloadImageViewTask != null)
			downloadImageViewTask.cancel(true);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_singleview_options, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_wallpaper) {
			ImageView imageView = (ImageView) imageSwitcher.getCurrentView();
			BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			WallPaperUtil.setWallpaper(this, bitmapDrawable.getBitmap());
			return true;
		} else if (id == R.id.action_set_favorite) {
			boolean isFavorite = sharedPreference.isPostFavorite(getApplicationContext(), mViewModel.getPost().getValue());
			if (!isFavorite) {
				item.setIcon(R.drawable.ic_turned_in_black_24dp);
				sharedPreference.addFavorite(this.getApplicationContext(), mViewModel.getPost().getValue());
				//Snackbar.make(getView(), getResources().getText(R.string.add_favr), Snackbar.LENGTH_SHORT).show();
			} else {
				item.setIcon(R.drawable.ic_turned_in_not_black_24dp);
				sharedPreference.removeFavorite(this.getApplicationContext(), mViewModel.getPost().getValue());
				//Snackbar.make(this.getBaseContext(), getResources().getText(R.string.remove_favr), Snackbar.LENGTH_SHORT).show();
			}
			return true;
		} else if (id == R.id.action_download) {
			//TODO resolve along with the task sch.
			//WallPaperUtil.downloadImage(this, this.mViewModel.getPost().getValue());
			return true;
		} else if (id == R.id.action_information) {
			displayInformationFragment();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void displayInformationFragment() {
		/*  Create information. fragment.    */
		try {
			InformationFragment informationFragment = InformationFragment.newInstance(Objects.requireNonNull(mViewModel.getPost().getValue()), POST_KEYS);
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

			transaction.replace(R.id.single_view_fragment_information_container, informationFragment);

/*			findViewById(R.id.single_view_fragment_information_container).setVisibility(View.VISIBLE);
			findViewById(R.id.image_switcher).setVisibility(View.VISIBLE)*/
			;

			transaction.addToBackStack(null);
			transaction.commit();/**/
		} catch (NullPointerException ignored) {

		}
	}

	public void fullScreen() {

		// BEGIN_INCLUDE (get_current_ui_flags)
		// The UI options currently enabled are represented by a bitfield.
		// getSystemUiVisibility() gives us that bitfield.
		int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
		int newUiOptions = uiOptions;
		// END_INCLUDE (get_current_ui_flags)
		// BEGIN_INCLUDE (toggle_ui_flags)
		boolean isImmersiveModeEnabled =
				((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
		if (isImmersiveModeEnabled) {
			Log.i(TAG, "Turning immersive mode mode off. ");
		} else {
			Log.i(TAG, "Turning immersive mode mode on.");
		}

		// Navigation bar hiding:  Backwards compatible to ICS.
		newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

		// Status bar hiding: Backwards compatible to Jellybean
		newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;

		// Immersive mode: Backward compatible to KitKat.
		// Note that this flag doesn't do anything by itself, it only augments the behavior
		// of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
		// all three flags are being toggled together.
		// Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
		// Sticky immersive mode differs in that it makes the navigation and status bars
		// semi-transparent, and the UI flag does not get cleared when the user interacts with
		// the screen.
		newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
		//END_INCLUDE (set_ui_flags)
	}

	private void setImageFullScreen(boolean fullscreen) {

		ImageView imageView = (ImageView) imageSwitcher.getCurrentView();
		if (fullscreen) {
			fullScreen();
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			tagListFragment.getView().setVisibility(View.GONE);

		} else {
			tagListFragment.getView().setVisibility(View.VISIBLE);
			imageView.setAdjustViewBounds(true);
		}
		isImageFitToScreen = fullscreen;
	}

/*			imageView.setSystemUiVisibility(
	View.SYSTEM_UI_FLAG_IMMERSIVE
	// Set the content to appear under the system bars so that the
	// content doesn't resize when the system bars hide and show.
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	// Hide the nav bar and status bar
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN);*/

}
