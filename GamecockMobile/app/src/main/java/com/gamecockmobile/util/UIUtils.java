package com.gamecockmobile.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.gamecockmobile.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gamecockmobile.util.LogUtils.*;

public class UIUtils {

  public static final String TAG = LogUtils.makeLogTag(UIUtils.class);

  public static final int ANIMATION_FADE_IN_TIME = 250;

  public static final String NEWS_IMAGE_VIEW_TAG = "news_image";

  /**
   * Create the track icon bitmap. Don't call this directly, instead use either
   * {@link com.gamecockmobile.util.UIUtils.NewsIconAsyncTask} or {@link UIUtils.NewsImageAsyncTask} to
   * asynchronously load the track icon.
   */
//  private static Bitmap createImageView(Context context, String trackName, int trackColor) {
//      final Resources res = context.getResources();
//      int iconSize = res.getDimensionPixelSize(R.dimen.track_icon_source_size);
//      Bitmap icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
//      Canvas canvas = new Canvas(icon);
//      Paint paint = new Paint();
//      paint.setAntiAlias(true);
//      paint.setColor(trackColor);
//      canvas.drawCircle(iconSize / 2, iconSize / 2, iconSize / 2, paint);
//
//       int iconResId = res.getIdentifier(
//              "track_" + ParserUtils.sanitizeId(trackName),
//              "drawable", context.getPackageName());
//      if (iconResId != 0) {
//          Drawable sourceIconDrawable = res.getDrawable(iconResId);
//          sourceIconDrawable.setBounds(0, 0, iconSize, iconSize);
//          sourceIconDrawable.draw(canvas);
//      }
//
//      return icon;
//  }
  /**
   * Synchronously get the track icon bitmap. Don't call this from the main thread, instead use
   * either {@link UIUtils.NewsIconAsyncTask} or {@link UIUtils.NewsImageAsyncTask} to
   * asynchronously load the track icon.
   */
  public static Bitmap getImageViewSync(Context ctx, String trackName, int trackColor) {

    if (TextUtils.isEmpty(trackName)) {
      return null;
    }

    // Find a suitable disk cache directory for the track icons and create if it doesn't
    // already exist.
//    File outputDir = ImageLoader.getDiskCacheDir(ctx, NEWS_IMAGE_VIEW_TAG);
//    if (!outputDir.exists()) {
//      outputDir.mkdirs();
//    }
//
//    // Generate a unique filename to store this track icon in using a hash function.
//    File imageFile = new File(outputDir + File.separator + hashKeyForDisk(trackName));
//
//    Bitmap bitmap = null;
//
//    // If file already exists and is readable, try and decode the bitmap from the disk.
//    if (imageFile.exists() && imageFile.canRead()) {
//      bitmap = BitmapFactory.decodeFile(imageFile.toString());
//    }
//
//    // If bitmap is still null here the track icon was not found in the disk cache.
//    if (bitmap == null) {
//
//      // Create the icon using the provided track name and color.
//      //bitmap = UIUtils.createImageView(ctx, trackName, trackColor);
//
//      // Now write it out to disk for future use.
//      BufferedOutputStream outputStream = null;
//      try {
//        outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//      } catch (FileNotFoundException e) {
//        // LOGE(TAG, "TrackIconAsyncTask - unable to open file - " + e);
//      } finally {
//        if (outputStream != null) {
//          try {
//            outputStream.close();
//          } catch (IOException ignored) {
//          }
//        }
//      }
    //}

    //return bitmap;
    return null;
  }

  /**
   * A hashing method that changes a string (like a URL) into a hash suitable for using as a disk
   * filename.
   */
  private static String hashKeyForDisk(String key) {
    String cacheKey;
    try {
      final MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(key.getBytes());
      cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
  }

  private static String bytesToHexString(byte[] bytes) {
    // http://stackoverflow.com/questions/332079
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xFF & bytes[i]);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  public static class NewsIconAsyncTask extends NewsImageAsyncTask {
    private WeakReference<ImageView> mImageViewReference;

    public NewsIconAsyncTask(ImageView imageView, String imageURL, BitmapCache bitmapCache) {
      super(imageURL, bitmapCache);

      // Store this AsyncTask in the tag of the ImageView so we can compare if the same task is
      // still running on this ImageView once processing is complete. This helps with view recycling
      // that takes place in a ListView type adapter.
      imageView.setTag(this);

      // If we have a BitmapCache, check if image view is available already.
      Bitmap bitmap = bitmapCache != null ? bitmapCache.getBitmapFromMemCache(imageURL) : null;

      // If found in BitmapCache set the Bitmap directly and cancel the task.
      if (bitmap != null) {
        imageView.setImageBitmap(bitmap);
        cancel(true);
      } else {
        // Otherwise clear the Imageview and store a WeakReference for later use. Better to use a
        // WeakReference here in case the task runs long and the holding activity or Fragment goes
        // away
        imageView.setImageDrawable(null);
        mImageViewReference = new WeakReference<ImageView>(imageView);
      }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onPostExecute(Bitmap bitmap) {
      ImageView imageView = mImageViewReference != null ? mImageViewReference.get() : null;

      // If ImageView is still around, bitmap processed OK and this task is not canceled.
      if (imageView != null && bitmap != null && !isCancelled()) {

        // Ensure this task is still the same one assigned to this ImageView, if not the view ws
        // likely recycled and a new task with a different icon is now running on the view and we
        // shouldn't proceed.
        if (this.equals(imageView.getTag())) {

          // On HC-MR1 run a quick fade-in animation.
          if (hasHoneycombMR1()) {
            imageView.setAlpha(0f);
            imageView.setImageBitmap(bitmap);
            imageView.animate().alpha(1f).setDuration(ANIMATION_FADE_IN_TIME).setListener(null);
          } else {
            // Before HC-MR! set the Bitmap directly.
            imageView.setImageBitmap(bitmap);
          }
        }
      }
    }
  }

  public static abstract class NewsImageAsyncTask extends AsyncTask<Context, Void, Bitmap> {
    private String mTrackName;
    private int mTrackColor;
    private BitmapCache mBitmapCache;

    public NewsImageAsyncTask(String trackName) {
      mTrackName = trackName;
    }

    public NewsImageAsyncTask(String trackName, BitmapCache bitmapCache) {
      mTrackName = trackName;
      mBitmapCache = bitmapCache;
    }

    @Override
   protected Bitmap doInBackground(Context... contexts) {

//      Bitmap bitmap = getTrackIconSync(contexts[0], mTrackName, mTrackColor);
//
//      // Store bitmap in memory cache for future use.
//      if (bitmap != null && mBitmapCache != null) {
//        mBitmapCache.addBitmapToCache(mTrackName, bitmap);
//      }
//
//      return bitmap;
        return null;
    }

    //protected abstract void onPostExecute(Bitmap bitmap);
 }

  public static boolean hasHoneycombMR1() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
  }
  
  public static boolean hasHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  public static boolean hasJellybean(){
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
  }
}
