package org.linuxsenpai.konachan.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.collection.LruCache;

import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.adapter.PostImagesAdapter;
import org.linuxsenpai.konachan.api.Cursor;
import org.linuxsenpai.konachan.db.Post;
import org.linuxsenpai.konachan.preference.SharedPreference;

public class FetchDisplayImageHolderItem extends AsyncTask<Void, Void, Void> {
	static final String TAG = "Download";
	PostImagesAdapter.ViewHolder viewHolder;
	Bitmap result;
	int offset;
	private Cursor<Post> postCursor;
	private LruCache<String, Bitmap> memoryCache;

	public FetchDisplayImageHolderItem(PostImagesAdapter.ViewHolder holder, LruCache<String, Bitmap> memoryCache, Cursor<Post> postCursor, int position) {
		this.viewHolder = holder;
		this.offset = position;
		this.memoryCache = memoryCache;
		this.postCursor = postCursor;
	}

	@Override
	protected Void doInBackground(Void... strings) {

		try {
			this.viewHolder.post = postCursor.getIndex(offset);
//			PostItem postItem = MetaController.getInstance().loadPost(offset);

			String imageId = String.valueOf(this.viewHolder.post.uid);

			//TODO add based on the cache.
			Bitmap cacheBitmap = memoryCache.get(imageId);

			if (cacheBitmap == null) {
				/*  */
				String preview_url = this.viewHolder.post.previewUrl;//postItem.GetPreviewURL();
				cacheBitmap = Network.GetBitMap(preview_url);
				memoryCache.put(imageId, cacheBitmap);
			}
			result = cacheBitmap;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		this.viewHolder.image.setImageBitmap(this.result);
		this.viewHolder.image.setVisibility(View.VISIBLE);
		this.viewHolder.progressbar.setVisibility(View.GONE);

		/*  TODO determine how to improve performance by not allocating everytime.  */
		SharedPreference sharedPreference = new SharedPreference();
		if (sharedPreference.isPostFavorite(this.viewHolder.image.getContext(), this.viewHolder.post)) {
			this.viewHolder.favoriteIcon.setVisibility(View.VISIBLE);
		}
	}

}
