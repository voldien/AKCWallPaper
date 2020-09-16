package org.linuxsenpai.konachan.Tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.collection.LruCache;

import org.linuxsenpai.konachan.Network;
import org.linuxsenpai.konachan.adapter.PostRecycleImageAdapter;
import org.linuxsenpai.konachan.api.Cursor;
import org.linuxsenpai.konachan.db.Post;

public class FetchDisplayImageHolderItem extends AsyncTask<Void, Void, Void> {
	PostRecycleImageAdapter.ViewHolder viewHolder;
	Bitmap result;
	int offset;
	private Cursor<Post> postCursor;
	private LruCache<String, Bitmap> memoryCache;


	public FetchDisplayImageHolderItem(PostRecycleImageAdapter.ViewHolder holder, LruCache<String, Bitmap> memoryCache, Cursor<Post> postCursor, int position) {
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
			Log.e("Download", e.getMessage());
		} finally {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		this.viewHolder.image.setImageBitmap(this.result);
		this.viewHolder.image.setVisibility(View.VISIBLE);
		this.viewHolder.progressbar.setVisibility(View.GONE);
	}

}
