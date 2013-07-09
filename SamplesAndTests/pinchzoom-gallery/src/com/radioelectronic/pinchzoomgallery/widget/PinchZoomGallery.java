package com.radioelectronic.pinchzoomgallery.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.widget.Gallery;

import com.radioelectronic.pinchzoomgallery.R;

/**
 * @author Artyom Kiriliyk
 * -------------------
 * Extends Android Gallery to open image onDoubleTap.
 */
public class PinchZoomGallery extends Gallery implements OnDoubleTapListener
{
	private Context c;

	public PinchZoomGallery(Context context, AttributeSet attrSet)
	{
		super(context, attrSet);
		c = context;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		TouchImageView imageViewGallery = (TouchImageView) inflate(c,
			R.layout.gallery_dialog, null);

		int position = super.getPositionForView(getSelectedView());

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inPurgeable = true;

		try
		{
			Bitmap bitmap = BitmapFactory.decodeStream(c.getAssets().open(
				"Images/"  + position + ".jpg"));
			imageViewGallery.setImageBitmap(bitmap);
			imageViewGallery.setMaxZoom(4f);

			Dialog d = new AlertDialog.Builder(c).setView(imageViewGallery)
				.setCancelable(true).create();
			d.setCanceledOnTouchOutside(true);
			d.show();
		}
		catch (Exception ex)
		{
			Log.e("Exception", ex.getLocalizedMessage());
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		return false;
	}
}