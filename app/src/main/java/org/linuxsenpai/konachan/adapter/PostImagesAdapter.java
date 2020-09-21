package org.linuxsenpai.konachan.adapter;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;

import org.linuxsenpai.konachan.DetailsTransition;
import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.Tasks.FetchDisplayImageHolderItem;
import org.linuxsenpai.konachan.api.Cursor;
import org.linuxsenpai.konachan.api.MetaController;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.fragment.SingleViewFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//TODO check if possible to repurpose it so it can be used with the favorite list as well.
public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder> {
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";
	private final Object diskCacheLock = new Object();
	private LruCache<String, Bitmap> memoryCache;
	private boolean diskCacheStarting = true;
	private Cursor<Post> postCursor;
	private ExecutorService executorService = null;

	public PostImagesAdapter(String query) {
		this.createRamCache();
		this.setPostTag(query);
		this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
			}

			@Override
			public void onItemRangeChanged(int positionStart, int itemCount) {
				super.onItemRangeChanged(positionStart, itemCount);
			}

			@Override
			public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
				super.onItemRangeChanged(positionStart, itemCount, payload);
			}

			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				super.onItemRangeInserted(positionStart, itemCount);
			}

			@Override
			public void onItemRangeRemoved(int positionStart, int itemCount) {
				super.onItemRangeRemoved(positionStart, itemCount);
			}

			@Override
			public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
				super.onItemRangeMoved(fromPosition, toPosition, itemCount);
			}
		});
	}

	private void createRamCache() {
		/*  Create memory allocation.   */
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;
		memoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	@Override
	public int getItemViewType(final int position) {
		return R.layout.item_image_view;
	}

	public void setPostTag(String query) {
		if (this.executorService != null) {
			executorService.shutdownNow();

		}
		executorService = new ThreadPoolExecutor(3, 128, 1,
				TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		//TODO resolve null param
		this.postCursor = MetaController.getInstance(null).getPostItems(query);
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,
	                                     int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
/*		layoutParams.height = (int) (parent.getHeight() * 0.15); // control the recyclerView row height from here
		view.setLayoutParams(layoutParams);*/
		ProgressBar progressBar = view.findViewById(R.id.item_image_view_processbar);
//		progressBar.startAnimation();
//		progressBar.startAnimation();
		//	new RotateAnimation()

		ImageView imageView = view.findViewById(R.id.item_image_view_image);
//		imageView.setImageResource(R.drawable.e4ecf82091e74124117661b645ece376);

		imageView.setMinimumWidth(0);
		imageView.setMinimumHeight(0);


		//	imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
/*		imageview.setLayoutParams
				(new ViewGroup.MarginLayoutParams
						(width, ViewGroup.LayoutParams.MATCH_PARENT));*/

		//Add animations for visual behaviors.
/*		imageView.setAnimation(AnimationUtils.loadAnimation(parent.getContext(), R.anim.fragment_close_enter));
		imageView.setAnimation(AnimationUtils.loadAnimation(parent.getContext(), R.anim.fragment_close_exit));*/

		//*  Create object.  *//*
		final ViewHolder vh = new ViewHolder(view, imageView, progressBar);

		//*  Reference the main click event. *//*
		imageView.setOnClickListener(new View.OnClickListener() {
			final ViewHolder holder = vh;

			@Override
			public void onClick(View v) {
				/*  Ignore click request intill the post result has been fetched.   */
				if (holder.post != null) {
					ImageView imageView = holder.image;

					//TODO add activiy or fragment instead of casting context.
					AppCompatActivity activity = (AppCompatActivity) v.getContext();
					FragmentManager fragmentManager = activity.getSupportFragmentManager();
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					transaction.setReorderingAllowed(true); //TODO setAllowOptimization before 26.1.0

					/*  Transition animation.   */
					Drawable previewImage = holder.image.getDrawable();
					SingleViewFragment singleViewFragment = SingleViewFragment.newInstance(postCursor, holder.post, previewImage);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						singleViewFragment.setSharedElementEnterTransition(new DetailsTransition());
						singleViewFragment.setEnterTransition(new Fade());
						//fragmentManager.findFragmentById(R.).setExitTransition(new Fade());
						singleViewFragment.setSharedElementReturnTransition(new DetailsTransition());
					}

					//singleViewFragment.setSharedElementEnterTransition(TransitionInflater.from(activity).inflateTransition(R.transition.change_image_transform));
					//singleViewFragment.setEnterTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.explode));

					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					transaction.addSharedElement(imageView, ViewCompat.getTransitionName(imageView));
					transaction.setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim);

					transaction.add(R.id.main_fragment, singleViewFragment);
					transaction.addToBackStack(null);
					transaction.commit();

				}
			}
		});

		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		FetchDisplayImageHolderItem fetchDisplayImageHolderItem = new FetchDisplayImageHolderItem(holder, memoryCache, this.postCursor, position);
		fetchDisplayImageHolderItem.executeOnExecutor(executorService);
	}

	@Override
	public int getItemCount() {
		return postCursor.size();//MetaController.getInstance().getCount();
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public Post post = null;
		public ImageView image;
		public ImageView favoriteIcon;
		public ProgressBar progressbar;

		public ViewHolder(View v, ImageView image, ProgressBar progressBar) {
			super(v);
			this.image = image;
			this.progressbar = progressBar;
			this.favoriteIcon = v.findViewById(R.id.item_image_view_favorite_icon);
		}
	}

}
