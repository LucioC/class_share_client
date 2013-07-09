package com.radioelectronic.pinchzoomgallery.adapter;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author Artyom Kiriliyk
 * -------------------
 * Custom adapter
 */
public class ImageAdapter extends BaseAdapter
{
	private Context context;
	private AssetManager assetManager;
	private String[] fileList;

	/** Simple Constructor saving the 'parent' context. */
	public ImageAdapter(Context c)
	{
		this.context = c;
		assetManager = context.getAssets();

		try
		{
			fileList = assetManager.list("Images");
		}
		catch (Exception e)
		{
			Log.e("Exception", e.getLocalizedMessage());
		}
	}

	/** Returns the amount of images we have defined. */
	@Override
	public int getCount()
	{
		return fileList.length;
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	/* Use the array-Positions as unique IDs */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/**
	 * Returns a new ImageView to be displayed, depending on the position passed.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView i = new ImageView(context);

		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(
				"Images/" + position + ".jpg"));
			i.setImageBitmap(bitmap);
		}
		catch (IOException e)
		{
			Log.e("Exception", e.getLocalizedMessage());
		}

		i.setPadding(1, 1, 1, 1);
		/* Image should be scaled as width/height are set. */
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		/* Set the Width/Height of the ImageView. */
		i.setLayoutParams(new Gallery.LayoutParams(150, 150));
		return i;
	}
}