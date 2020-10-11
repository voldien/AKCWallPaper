package org.linuxsenpai.konachan.fragment;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Fade;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.linuxsenpai.konachan.DetailsTransition;
import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.tasks.DownloadImageViewTask;
import org.linuxsenpai.konachan.tasks.SetTagListTask;
import org.linuxsenpai.konachan.api.Cursor;
import org.linuxsenpai.konachan.db.AppDatabase;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.preference.SharedPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SingleViewFragment extends Fragment {

	static public final String META_KEY = "";
	private static final String ARG_POST_OBJECT = "post-item-object";
	private static final String[] POST_KEYS = {"id", "tags", "created-date", "author", "source", "score", "file size", "file url", "preview url"};
	DownloadImageViewTask downloadImageViewTask = null;
	SetTagListTask setTagListTask = null;
	private SingleViewViewModel mViewModel;
	private TagListFragment tagListFragment;
	private ImageSwipeFragment imageSwipeFragment;
	private Drawable drawable;
	private MenuItem bookmarkMenu;
	private SharedPreference sharedPreference;

	public static SingleViewFragment newInstance(Cursor<Post> cursor, Post post, Drawable previewImage) {
		SingleViewFragment singleViewFragment = new SingleViewFragment();
		singleViewFragment.drawable = previewImage;
		Bundle bundle = new Bundle();
		bundle.putParcelable(ARG_POST_OBJECT, post);
		singleViewFragment.setArguments(bundle);
		return singleViewFragment;
	}

	public static String insertImage(ContentResolver cr,
	                                 Bitmap source,
	                                 String title,
	                                 String description) {

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, title);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
		values.put(MediaStore.Images.Media.DESCRIPTION, description);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		// Add the date meta data to ensure the image is added at the front of the gallery
		values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
		}

		Uri url = null;
		String stringUrl = null;    /* value to be returned */

		try {
			url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			if (source != null) {
				try (OutputStream imageOut = cr.openOutputStream(url)) {
					source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				}

				long id = ContentUris.parseId(url);
				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
				// This is for backward compatibility.
				storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	}

	/**
	 * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
	 * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
	 * meta data. The StoreThumbnail method is private so it must be duplicated here.
	 *
	 * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
	 */
	private static Bitmap storeThumbnail(
			ContentResolver cr,
			Bitmap source,
			long id,
			float width,
			float height,
			int kind) {

		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
				source.getWidth(),
				source.getHeight(), matrix,
				true
		);

		ContentValues values = new ContentValues(4);
		values.put(MediaStore.Images.Thumbnails.KIND, kind);
		values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
		values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
		values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

		Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);
			thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		/*  */
		final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_ImageView);
		inflater = inflater.cloneInContext(contextThemeWrapper);
		View view = inflater.inflate(R.layout.fragment_single_view, container, false);

		/*  */
		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_48px);
			actionBar.setTitle("");
		}

		/*  Enable back button. */
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		/*  TODO resolve animation and layout.  */
		FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_action_button);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		/*  TODO add scroll animation transition effects.   */
		ScrollView scrollView = view.findViewById(R.id.scroll_view);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
				@Override
				public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

				}


			});
		}

		//tagListFragment.setTagList();

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_48px);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreference = new SharedPreference();

		/*  Add the fragments.  */
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		setEnterSharedElementCallback(new SharedElementCallback() {
			@Override
			public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
				super.onMapSharedElements(names, sharedElements);
			}
		});
		transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
		this.imageSwipeFragment = ImageSwipeFragment.newInstance(drawable);
		this.tagListFragment = TagListFragment.newInstance(1);
		transaction.replace(R.id.single_view_fragment_imageswipe_container, this.imageSwipeFragment);
		transaction.replace(R.id.single_view_fragment_information_container, this.tagListFragment);
		transaction.addToBackStack(null);
		transaction.commit();/**/
		//

		this.mViewModel = new ViewModelProvider(this).get(SingleViewViewModel.class);
		this.mViewModel.getPost().observe(this, new Observer<Post>() {
			@Override
			public void onChanged(Post post) {
				displayPostItem(post);
				Log.d("SingleView", post.toString());

			}
		});

		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//this.imageSwipeFragment.getView().setOnTouchListener(new SingleViewImageSwipeOnTouch(this, imageSwipeFragment.imageSwitcher));

		if (getArguments() != null) {
			Post post = getArguments().getParcelable(ARG_POST_OBJECT);
			this.mViewModel.setPostItem(post);
		}
	}

	@Override
	public void onAttachFragment(@NonNull Fragment childFragment) {
		super.onAttachFragment(childFragment);
	}

	public void displayPostItem(final Post post) {

		/*  Check and update parent reference view. */
		//TODO improve
		if (post.parent_id > 0) {
			List<Post> parentList = AppDatabase.getAppDatabase(getContext()).postDao().loadAllByIds(new int[]{post.parent_id});
			if (parentList.size() > 0) {
				TextView textView = this.getView().findViewById(R.id.textview_child_link);
				textView.setVisibility(View.VISIBLE);
				textView.setOnClickListener(new View.OnClickListener() {
					final private Post _post = post;

					@Override
					public void onClick(View v) {
						List<Post> postList = AppDatabase.getAppDatabase(getContext()).postDao().loadAllByIds(new int[]{_post.parent_id});
						if (postList.size() > 0) {
							imageSwipeFragment.imageSwitcher.getNextView();
							displayPostItem(postList.get(0));
						}
					}
				});
			}
		} else {
			TextView textView = this.getView().findViewById(R.id.textview_child_link);
			textView.setVisibility(View.GONE);
		}

		imageSwipeFragment.getView().setOnTouchListener(new View.OnTouchListener() {
			private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onDoubleTap(MotionEvent e) {
					Log.d("TEST", "onDoubleTap");


					return super.onDoubleTap(e);
				}
				//    ... // implement here other callback methods like onFling, onScroll as necessary
			});

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				gestureDetector.onTouchEvent(event);
				return true;
			}


		});

		/*  Set if bookmarked as favorite.  */
		if (bookmarkMenu != null) {
			SharedPreference sharedPreference = new SharedPreference();
			if (sharedPreference.isPostFavorite(this.getContext(), post)) {
				bookmarkMenu.setIcon(R.drawable.ic_turned_in_black_24dp);
			} else {
				bookmarkMenu.setIcon(R.drawable.ic_turned_in_not_black_24dp);
			}
		}

		/*  Download the image. */
		downloadImageViewTask = new DownloadImageViewTask((ImageView) imageSwipeFragment.getCurrentImageView());
		downloadImageViewTask.execute(post.sampleUrl);

		/*  Download and fetch the tags.    */
		setTagListTask = new SetTagListTask(tagListFragment);
		setTagListTask.execute(post);


		//TODO resolve later.
		SetImageFullScreen();
		/*  TODO add notes. */
	}

	@Override
	public void onPause() {
		super.onPause();
		if (setTagListTask != null)
			setTagListTask.cancel(true);

	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		/*  Clear and add a new menu.   */
		menu.clear();
		inflater.inflate(R.menu.menu_singleview_options, menu);
		this.bookmarkMenu = menu.findItem(R.id.action_bookmark);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_wallpaper:
				ImageView imageView = (ImageView) imageSwipeFragment.imageSwitcher.getCurrentView();
				BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
				setWallpaper(bitmapDrawable.getBitmap());
				return true;
			case R.id.action_bookmark:
				boolean isFavorite = sharedPreference.isPostFavorite(getContext(), mViewModel.getPost().getValue());
				if (!isFavorite) {
					item.setIcon(R.drawable.ic_turned_in_black_24dp);
					sharedPreference.addFavorite(this.getContext(), mViewModel.getPost().getValue());
					Snackbar.make(getView(), getResources().getText(R.string.add_favr), Snackbar.LENGTH_SHORT).show();
				} else {
					item.setIcon(R.drawable.ic_turned_in_not_black_24dp);
					sharedPreference.removeFavorite(this.getContext(), mViewModel.getPost().getValue());
					Snackbar.make(getView(), getResources().getText(R.string.remove_favr), Snackbar.LENGTH_SHORT).show();
				}
				return true;
			case R.id.action_download:
				DownloadImage();
				return true;
			case R.id.action_information:
				DisplayInformationFragment();
				return true;
			case R.id.action_tag_information:
				return true;
			case R.id.action_search:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void DisplayInformationFragment() {
		/*  Create information. fragment.    */
		InformationFragment informationFragment = InformationFragment.newInstance(Objects.requireNonNull(mViewModel.getPost().getValue()), POST_KEYS);
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);
		transaction.add(R.id.main_fragment, informationFragment);
		transaction.addToBackStack(null);
		transaction.commit();/**/
	}

	private void DownloadImage() {

		/*  Get bitmap of the current view. */
		ImageSwipeFragment imageSwipeFragment = (ImageSwipeFragment) getChildFragmentManager().findFragmentById(R.id.single_view_fragment_imageswipe_container);
		ImageView imageView = (ImageView) imageSwipeFragment.imageSwitcher.getCurrentView();
		Bitmap bmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

		/*      */
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {

//					MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmap, "Image Anime", "yourDescription");
			insertImage(getContext().getContentResolver(), bmap, "", "");
//					ContentValues contentValues =  new ContentValues();
/*					c.apply {
						put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
						put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
							put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
							put(MediaStore.MediaColumns.IS_PENDING, 1)
						}
					}*/
			Snackbar.make(getView(), getResources().getText(R.string.download_image), Snackbar.LENGTH_SHORT).show();
			//Toast.makeText(getContext(), "Image Saved to local storage.", Toast.LENGTH_SHORT).show();
		} else if (shouldShowRequestPermissionRationale("")) {
			// In an educational UI, explain to the user why your app requires this
			// permission for a specific feature to behave as expected. In this UI,
			// include a "cancel" or "no thanks" button that allows the user to
			// continue using your app without granting the permission.
//			showInContextUI(...);
		} else {
			// You can directly ask for the permission.
			requestPermissions(
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					0);
		}
	}

	private void SetImageFullScreen() {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setReorderingAllowed(true); //TODO setAllowOptimization before 26.1.0
		FullScreenFragment singleViewFragment = FullScreenFragment.newInstance();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			singleViewFragment.setSharedElementEnterTransition(new DetailsTransition());
			singleViewFragment.setEnterTransition(new Fade());
			setExitTransition(new Fade());
			singleViewFragment.setSharedElementReturnTransition(new DetailsTransition());
		}
		transaction.addSharedElement(imageSwipeFragment.getCurrentImageView(), ViewCompat.getTransitionName(imageSwipeFragment.getCurrentImageView()));
		transaction.add(R.id.main_fragment, singleViewFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void setWallpaper(Bitmap bitmap) {
/*		Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(getImageUri(this, bitmap), "image/jpeg");
		intent.putExtra("mimeType", "image/jpeg");
		this.startActivity(Intent.createChooser(intent, "Set as:"));*/


//				Uri uri =   Uri.parse("android.resource://your.package.here/drawable/image_name")
/*
		WallpaperManager myWallpaperManager
				= WallpaperManager.getInstance(getContext());
*//*				myWallpaperManager.isSetWallpaperAllowed();
				myWallpaperManager.isWallpaperSupported();*//*
		try {
			//Intent.ACTION_SET_WALLPAPER
			ImageView imageView = (ImageView) imageSwitcher.getCurrentView();
			Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
			//myWallpaperManager.setBitmap(bitmap);
			Uri myImageUri = getImageUri(this, bitmap);
			Intent intent = new Intent(myWallpaperManager.getCropAndSetWallpaperIntent(myImageUri));
			startActivity(intent);
			*//*					*//*
		 *//*					myWallpaperManager.clear();
					ImageView imageView = (ImageView)imageSwitcher.getCurrentView();
					Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();*//*


		} finally {
			Toast.makeText(this, "WallPaper Updated", Toast.LENGTH_SHORT).show();
		}*/
	}


}
