package org.linuxsenpai.konachan.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import org.linuxsenpai.konachan.R;
import org.linuxsenpai.konachan.WallPaperUtil;
import org.linuxsenpai.konachan.activity.SingleViewActivity;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.preference.SharedPreference;
import org.linuxsenpai.konachan.tasks.FetchDisplayImageHolderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.linuxsenpai.konachan.activity.SingleViewActivity.ARG_POST_OBJECT;

public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.PostViewHolder> {
	private final List<Post> data;
	//TODO add support later - Disk Cache.
/*	private DiskLruCache diskLruCache;
	private final Object diskCacheLock = new Object();
	private boolean diskCacheStarting = true;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";*/

	private LruCache<String, Bitmap> memoryCache;
	private ExecutorService executorService = null;

	public PostImagesAdapter() {
		this.data = new ArrayList<>();
		this.createRamCache();
		if (this.executorService != null) {
			executorService.shutdownNow();
		}
		executorService = new ThreadPoolExecutor(3, 128, 1,
				TimeUnit.SECONDS, new LinkedBlockingQueue<>());

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

	@Override
	public PostViewHolder onCreateViewHolder(ViewGroup parent,
	                                         int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
		return new PostViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PostViewHolder holder, int position) {
		FetchDisplayImageHolderItem fetchDisplayImageHolderItem = new FetchDisplayImageHolderItem(holder, memoryCache, position);
		fetchDisplayImageHolderItem.executeOnExecutor(executorService, data.get(position));
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItems(List<Post> list) {
		this.data.addAll(list);
		this.notifyDataSetChanged();
	}

	public void clear() {
		this.data.clear();
		this.notifyDataSetChanged();
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class PostViewHolder extends RecyclerView.ViewHolder {
		public final ImageView imageView;
		public final ImageView favoriteIcon;
		public final ProgressBar progressbar;
		// each data item is just a string in this case
		public Post post = null;

		public PostViewHolder(View v) {
			super(v);
			this.imageView = v.findViewById(R.id.item_image_view_image);
			this.progressbar = v.findViewById(R.id.item_image_view_processbar);
			this.favoriteIcon = v.findViewById(R.id.item_image_view_favorite_icon);
			this.favoriteIcon.setVisibility(View.GONE);


			imageView.setMinimumWidth(0);
			imageView.setMinimumHeight(0);

			/*  */
			this.favoriteIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getVisibility() == View.VISIBLE)
						v.setVisibility(View.GONE);
					else
						v.setVisibility(View.VISIBLE);
				}
			});

			//*  Reference the main click event. *//*
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*  Ignore click request intill the post result has been fetched.   */
					if (post != null) {

						AppCompatActivity activity = (AppCompatActivity) v.getContext();
						if (activity != null) {
							Pair[] pairs = new Pair[1];
							pairs[0] = new Pair(imageView, "THUMBNAIL_IMAGE");

							ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
							Intent in = new Intent(v.getContext(), SingleViewActivity.class);
							Bundle bundle = new Bundle();
							bundle.putParcelable(ARG_POST_OBJECT, post);
							in.putExtras(bundle);
							activity.startActivity(in, activityOptionsCompat.toBundle());
						}
					}
				}
			});

			imageView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (post != null) {
						PopupMenu popup = new PopupMenu(imageView.getContext(), v);
						MenuInflater inflater = popup.getMenuInflater();
						inflater.inflate(R.menu.menu_image_options, popup.getMenu());

						popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								//TODO handle since there will be multiple instance where this code will be used.

								AppCompatActivity appCompatActivity = (AppCompatActivity) imageView.getContext();
								BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
								switch (item.getItemId()) {
									case R.id.action_download_image:
										WallPaperUtil.saveBitmap(appCompatActivity, bitmapDrawable.getBitmap(), post);
										break;
									case R.id.action_set_wallpaper:

										WallPaperUtil.setWallpaper(appCompatActivity, bitmapDrawable.getBitmap());
										break;
									case R.id.action_favorite:
										setFavorite(true);
										break;
									default:
										break;
								}
								return true;
							}
						});

						popup.show();
						return true;
					}
					return false;
				}
			});

			//TODO add view animations on click and etc.
/*			int[] attrs = new int[]{R.attr.selectableItemBackground};
			TypedArray typedArray = v.getContext().obtainStyledAttributes(attrs);
			int backgroundResource = typedArray.getResourceId(0, 0);
			image.setBackgroundResource(backgroundResource);*/
		}

		public void bindTo(Post post, Bitmap bitmap) {
			this.imageView.setImageBitmap(bitmap);
			this.imageView.setVisibility(View.VISIBLE);
			this.progressbar.setVisibility(View.GONE);

			/*  TODO determine how to improve performance by not allocating everytime.  */
			SharedPreference sharedPreference = new SharedPreference();
			if (sharedPreference.isPostFavorite(this.imageView.getContext(), this.post)) {
				this.favoriteIcon.setVisibility(View.VISIBLE);
			} else
				this.favoriteIcon.setVisibility(View.GONE);
		}

		public void setFavorite(boolean status) {
			if (post != null) {
				SharedPreference s = new SharedPreference();
				/*  Update the view.    */
				if (status) {
					s.addFavorite(imageView.getContext(), post);
					this.favoriteIcon.setVisibility(View.VISIBLE);
				} else {
					s.removeFavorite(imageView.getContext(), post);
					this.favoriteIcon.setVisibility(View.GONE);
				}
			}
		}
	}

}
