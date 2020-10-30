package org.linuxsenpai.konachan;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import org.linuxsenpai.konachan.db.Post;

import java.io.IOException;
import java.io.OutputStream;

//TODO rename to something more fitting.
public class WallPaperUtil {

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

	public static void setWallpaper(Activity activity, Bitmap bitmap) {
/*		Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setDataAndType(getImageUri(this, bitmap), "image/jpeg");
		intent.putExtra("mimeType", "image/jpeg");
		this.startActivity(Intent.createChooser(intent, "Set as:"));*/

		//TODO add support for a crop window
		Context context = activity.getBaseContext();
		WallpaperManager myWallpaperManager
				= WallpaperManager.getInstance(context);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			myWallpaperManager.isSetWallpaperAllowed();
			myWallpaperManager.isWallpaperSupported();
		}
		try {
			myWallpaperManager.setBitmap(bitmap);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Toast.makeText(activity, "WallPaper Updated", Toast.LENGTH_SHORT).show();
		}
	}

	public static void saveBitmap(Activity activity, Bitmap bitmap, Post post) {
		Context context = activity.getBaseContext();
		/*      */
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
			insertImage(context.getContentResolver(), bitmap, "", "");
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (activity.shouldShowRequestPermissionRationale("")) {
			} else {
				// You can directly ask for the permission.
				activity.requestPermissions(
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						0);
			}
		}
	}

	//TODO rename
	public static void downloadImage(Activity activity, Post post) {
		Context context = activity.getBaseContext();
		Bitmap bmap = Network.GetBitMap(post.file_url);
		saveBitmap(activity, bmap, post);
	}
}
