package org.linuxsenpai.konachan.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.collection.LruCache;

import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.adapter.PostImagesAdapter;
import org.linuxsenpai.konachan.db.Post;

public class FetchDisplayImageHolderItem extends AsyncTask<Post, Void, Void> {
	static final String TAG = "Download";
	final PostImagesAdapter.PostViewHolder postViewHolder;
	final int offset;
	private final LruCache<String, Bitmap> memoryCache;
	Bitmap result;

	public FetchDisplayImageHolderItem(PostImagesAdapter.PostViewHolder holder, LruCache<String, Bitmap> memoryCache, int position) {
		this.postViewHolder = holder;
		this.offset = position;
		this.memoryCache = memoryCache;
	}

	@Override
	protected Void doInBackground(Post... strings) {

		try {
			this.postViewHolder.post = strings[0];

			String imageId = String.valueOf(this.postViewHolder.post.uid);
			//TODO add based on the cache.
			Bitmap cacheBitmap = memoryCache.get(imageId);

			if (cacheBitmap == null) {
				/*  */
				String preview_url = this.postViewHolder.post.previewUrl;
				cacheBitmap = Network.GetBitMap(preview_url);
				memoryCache.put(imageId, cacheBitmap);
			}
			result = cacheBitmap;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		if (this.result != null && this.postViewHolder.post != null) {
			this.postViewHolder.bindTo(this.postViewHolder.post, this.result);
		}
	}
}
