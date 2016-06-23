package com.hcpt.fastfood.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * This utility class contains all image operations
 * 
 * @author |dmb TEAM|
 * 
 */
public class ImageUtil {
	// 16:9
	// public static final double aspectRation = 1.77;
	// 4:3
	/**
	 * Constant for the aspect ratio.
	 */
	public static final double aspectRationSlider = 1.77;
	public static final double aspectRationThumb = 1.5;

	public static int widthForSlider;
	public static int widthForThumbs;

	/**
	 * Constant for the maximum size of image thumbnails.
	 */
	public static final int MAX_IMAGE_SIZE_THUMNAILS = 150;

	/**
	 * Creates Bitmap image by given URL.
	 * 
	 * @param url
	 *            url of the image
	 * @return <code>Bitmap</code> object with the image
	 */
	public static Bitmap createBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(url)
					.getContent());
		} catch (MalformedURLException e) {
			//
		} catch (IOException e) {
			//
		}

		return bitmap;
	}

	/**
	 * Creates thumbnail bitmap from given URL
	 * 
	 * @param url
	 *            <code>String</code> object containing the URL
	 * @return <code>Bitmap</code> object
	 */
	public static Bitmap createThumbBitmapFromUrl(String url) {

		InputStream stream = null;
		try {
			stream = (InputStream) new URL(url).getContent();
		} catch (MalformedURLException e) {
			//
		} catch (IOException e) {
			//
		}

		Bitmap bitmap = null;

		bitmap = BitmapFactory.decodeStream(stream);

		double newBitmapHeight = (double) MAX_IMAGE_SIZE_THUMNAILS
				/ aspectRationSlider;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outWidth = MAX_IMAGE_SIZE_THUMNAILS;
		options.outHeight = (int) newBitmapHeight;

		bitmap = Bitmap.createScaledBitmap(bitmap, MAX_IMAGE_SIZE_THUMNAILS,
				(int) newBitmapHeight, false);

		return bitmap;
	}

	/**
	 * Fixes the bitmap size for specific screens.
	 * 
	 * @param stream
	 *            input stream to fetch the bitmap
	 * @param screenWidth
	 *            widh of the screen
	 * @param url
	 *            url of the image
	 * @return <code>Bitmap</code> object with the image with fixed size
	 */
	public static Bitmap fixBitmapForSpecificScreen(InputStream stream,
			Integer screenWidth) {

		Bitmap bitmap = null;

		bitmap = BitmapFactory.decodeStream(stream);

		double newBitmapHeight = (double) screenWidth / aspectRationSlider;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outWidth = screenWidth;
		options.outHeight = (int) newBitmapHeight;

		bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,
				(int) newBitmapHeight, false);

		return bitmap;
	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}

}
