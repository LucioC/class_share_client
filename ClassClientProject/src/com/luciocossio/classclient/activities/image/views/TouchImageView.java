package com.luciocossio.classclient.activities.image.views;

import com.luciocossio.classclient.listeners.ImageMoveZoomPanListener;
import com.luciocossio.gestures.detectors.RotationGestureDetector;
import com.luciocossio.gestures.listeners.RotationListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Artyom Kiriliyk
 * adapted from : https://github.com/radioelectronic/pinchzoom-gallery
 * -------------------
 * Extends Android ImageView to include pinch zooming and panning.
 */
public class TouchImageView extends ImageView
{
	Matrix matrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int ROTATION = 3;
	int mode = NONE;

	// Remember some things for zooming
	PointF last = new PointF();
	PointF start = new PointF();
	float minScale = 1f;
	float maxScale = 3f;
	float[] m;

	float redundantXSpace, redundantYSpace;

	float width, height;
	static final int CLICK = 3;
	float saveScale = 1f;
	float right, bottom, origWidth, origHeight, bmWidth, bmHeight;
	
	ScaleGestureDetector mScaleDetector;
	
	private RotationGestureDetector rotationDetector;
	private RotationListener rotationListener;

	Context context;
	
	ScaleListener scaleListener = null;
	ImageMoveZoomPanListener listener = null;
	
	public TouchImageView(Context context)
	{
		super(context);
		sharedConstructing(context);
	}

	public TouchImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		sharedConstructing(context);
	}

	private void sharedConstructing(Context context)
	{		
		super.setClickable(true);
		this.context = context;
		scaleListener = new ScaleListener();
		scaleListener.setImageListener(listener);
		mScaleDetector = new ScaleGestureDetector(context, scaleListener);
		matrix.setTranslate(1f, 1f);
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);
		createRotationDetector();
		registerListener(null);
	}
	
	public void registerListener(GestureDetectorCompat newDetector)
	{
		final GestureDetectorCompat newRegisteredDetector = newDetector;
		setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				mScaleDetector.onTouchEvent(event);				
				rotationDetector.onTouchEvent(event);
				
				if(newRegisteredDetector != null)
					newRegisteredDetector.onTouchEvent(event);
				
				matrix.getValues(m);
				PointF curr = new PointF(event.getX(), event.getY());
				
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						last.set(event.getX(), event.getY());
						start.set(last);
						mode = DRAG;
						break;
					case MotionEvent.ACTION_MOVE:
						if (mode == DRAG)
						{
							updateImagePosition(curr, last, false);
						}
						break;

					case MotionEvent.ACTION_UP:
						mode = NONE;
						int xDiff = (int) Math.abs(curr.x - start.x);
						int yDiff = (int) Math.abs(curr.y - start.y);
						if (xDiff < CLICK && yDiff < CLICK)
							performClick();
						if(getImageScale() <= 1)
							resizeAndCentralize();
						break;

					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
				}
				setImageMatrix(matrix);							
				
				invalidate();
				return true; // indicate event was handled
			}
			
			
		});
	}
	
	protected void createRotationDetector()
	{
		final TouchImageView imageView = this;
		final int minimumAngle = 12;
		rotationListener = new RotationListener(minimumAngle)
		{
			private boolean rotated = false;
			
			@Override
			public boolean onRotationStart() {
				rotated = false;
				return super.onRotationStart();
			}
			
			@Override
			public boolean onRotation(float angleChange, float distanceChange) {
				
				if(imageView.getImageScale() > 1) return false;
				if(mode == ZOOM) return false;
				
				if(Math.abs(angleChange) > Math.abs(distanceChange))
				{
					mode = ROTATION;
				}
				
				return super.onRotation(angleChange, distanceChange);				
			}
			
			@Override
			protected void rotationChange(float angleChange,
					float accumulatedAngle, float lastAngle) {
				
				if(rotated) return;
				
				if(angleChange >= minimumAngle)
				{
					imageView.setImageBitmap(imageView.getImageBitmap(), imageView.getRotationDegrees() - 90);
					rotated = true;
				}
				else if(angleChange <= -minimumAngle)
				{
					imageView.setImageBitmap(imageView.getImageBitmap(), imageView.getRotationDegrees() + 90);
					rotated = true;
				}
				imageView.invalidate();
			}			
		};
		rotationDetector = new RotationGestureDetector(rotationListener);		
	}
		
	public PointF getImagePoint()
	{
		matrix.getValues(m);
		float x = m[Matrix.MTRANS_X];
		float y = m[Matrix.MTRANS_Y];
		return new PointF(x,y);
	}
	
	public PointF getImageCenterPoint()
	{
		return new PointF(redundantXSpace,redundantYSpace);
	}

	public void updateImagePosition(PointF curr, PointF last) {
		updateImagePosition(curr, last, true);
	}	
	
	public void updateImagePosition(PointF curr, PointF last, boolean verticalMove) {
		
		matrix.getValues(m);
		float x = m[Matrix.MTRANS_X];
		float y = m[Matrix.MTRANS_Y];
		float deltaX = curr.x - last.x;
		float deltaY = curr.y - last.y;
		float scaleWidth = Math.round(origWidth * saveScale);
		float scaleHeight = Math.round(origHeight * saveScale);
		if (scaleWidth < width)
		{
			deltaX = 0;
			if (y + deltaY > 0)
				deltaY = -y;
			else if (y + deltaY < -bottom)
				deltaY = -(y + bottom);
		}
		else if (scaleHeight < height)
		{
			if(!verticalMove)
				deltaY = 0;
			if (x + deltaX > 0)
				deltaX = -x;
			else if (x + deltaX < -right)
				deltaX = -(x + right);
		}
		else
		{
			if (x + deltaX > 0)
				deltaX = -x;
			else if (x + deltaX < -right)
				deltaX = -(x + right);

			if (y + deltaY > 0)
				deltaY = -y;
			else if (y + deltaY < -bottom)
				deltaY = -(y + bottom);
		}
		
		matrix.postTranslate(deltaX, deltaY);							
		
		updateVisiblePartDimensions();	
		
		last.set(curr.x, curr.y);
		setImageMatrix(matrix);
	}			
	
	public void setListener(ImageMoveZoomPanListener newListener)
	{
		this.listener = newListener;		
		scaleListener.setImageListener(listener);
	}
	
	public Bitmap getImageBitmap()
	{
		return imageBitmap;		
	}
	
	private Bitmap imageBitmap = null;
	
	@Override
	public void setImageBitmap(Bitmap bm)
	{
		this.imageBitmap = bm;
		super.setImageBitmap(bm);
		if (bm != null)
		{
			bmWidth = bm.getWidth();
			bmHeight = bm.getHeight();
		}
	}
	
	private int rotationDegrees = 0;

	public void setImageBitmap(Bitmap bm, int degrees)
	{
		this.rotationDegrees = degrees;
		bm = copyBitmapWithRotation(bm, degrees);
		super.setImageBitmap(bm);
		if (bm != null)
		{
			bmWidth = bm.getWidth();
			bmHeight = bm.getHeight();
		}
	}

	public void setMaxZoom(float x)
	{
		maxScale = x;
	}
	
	protected void updateVisiblePartDimensions() {
		//get real view that is being showed on screen
		matrix.getValues(m);
		float left = m[Matrix.MTRANS_X];
		float top = m[Matrix.MTRANS_Y];
		if(top > 0) top = 0;
		float scale = m[Matrix.MSCALE_X];
		float right = -left + getWidth();
		right = (right!=0) ? right/scale : 0;
		float bottom = -top + getHeight();
		bottom = (bottom!=0) ? bottom/scale : 0;	
		if(bottom > bmHeight) bottom = bmHeight;

		left = (left!=0) ? left/scale : 0;
		top = (top!=0) ? top/scale : 0;
		
		listener.setImageAngle(this.rotationDegrees);
		
		//Log.i("NEWVISIBLEPART", left + ":" + top + ":" + right + ":" + bottom);
		//Log.i("ZOOM", scale + "");
		//Log.i("IMAGE", this.getDrawable().getBounds().width() + ":" + this.getDrawable().getBounds().height());
		listener.updateVisiblePart((int)Math.abs(left), (int)Math.abs(top), (int)Math.abs(right), (int)Math.abs(bottom), this.getDrawable().getBounds().height(), this.getDrawable().getBounds().width());
	}

	private class ScaleListener extends
		ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		ImageMoveZoomPanListener listener;
		
		public void setImageListener(ImageMoveZoomPanListener listener)
		{
			this.listener = listener;
		}
		
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			if(mode != ROTATION)
				mode = ZOOM;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			if(mode != ZOOM) return false;
			
			float mScaleFactor = (float) Math.min(
				Math.max(.95f, detector.getScaleFactor()), 1.05);
			float origScale = saveScale;
			saveScale *= mScaleFactor;
			if (saveScale > maxScale)
			{
				saveScale = maxScale;
				mScaleFactor = maxScale / origScale;
			}
			else if (saveScale < minScale)
			{
				saveScale = minScale;
				mScaleFactor = minScale / origScale;
			}
			right = width * saveScale - width - (2 * redundantXSpace * saveScale);
			bottom = height * saveScale - height
				- (2 * redundantYSpace * saveScale);
			if (origWidth * saveScale <= width || origHeight * saveScale <= height)
			{
				matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);

				if (mScaleFactor < 1)
				{
					matrix.getValues(m);
					float x = m[Matrix.MTRANS_X];
					float y = m[Matrix.MTRANS_Y];
					if (mScaleFactor < 1)
					{
						if (Math.round(origWidth * saveScale) < width)
						{
							if (y < -bottom)
							{
								matrix.postTranslate(0, -(y + bottom));
							}
							else if (y > 0)
							{
								matrix.postTranslate(0, -y);
							}
						}
						else
						{
							if (x < -right)
							{
								matrix.postTranslate(-(x + right), 0);
							}
							else if (x > 0)
							{
								matrix.postTranslate(-x, 0);
							}
						}
					}
				}
			}
			else
			{
				matrix.postScale(mScaleFactor, mScaleFactor, (int)detector.getFocusX(),
					(int)detector.getFocusY());
				
				matrix.getValues(m);
				float x = m[Matrix.MTRANS_X];
				float y = m[Matrix.MTRANS_Y];
				if (mScaleFactor < 1)
				{
					if (x < -right)
						matrix.postTranslate(-(x + right), 0);
					else if (x > 0)
						matrix.postTranslate(-x, 0);
					if (y < -bottom)
						matrix.postTranslate(0, -(y + bottom));
					else if (y > 0)
						matrix.postTranslate(0, -y);
				}
			}
			updateVisiblePartDimensions();
			return true;
		}
	}
	
	public float getImageScale()
	{
		return saveScale;		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		resizeAndCentralize();
	}

	protected void resizeAndCentralize() {
		// Fit to screen.
		float scale;
		float scaleX = (float) width / (float) bmWidth;
		float scaleY = (float) height / (float) bmHeight;
		scale = Math.min(scaleX, scaleY);
		matrix.setScale(scale, scale);
		setImageMatrix(matrix);
		saveScale = 1f;

		// Center the image
		redundantYSpace = (float) height - (scale * (float) bmHeight);
		redundantXSpace = (float) width - (scale * (float) bmWidth);
		redundantYSpace /= (float) 2;
		redundantXSpace /= (float) 2;

		matrix.postTranslate(redundantXSpace, redundantYSpace);

		origWidth = width - 2 * redundantXSpace;
		origHeight = height - 2 * redundantYSpace;
		right = width * saveScale - width - (2 * redundantXSpace * saveScale);
		bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
		setImageMatrix(matrix);
	}
	
	public Bitmap copyBitmapWithRotation(Bitmap bitmap, int degrees)
	{		
		Matrix matrix = new Matrix();
		matrix.preRotate(degrees, bitmap.getWidth()/2, bitmap.getHeight()/2);
		Bitmap rotatedBitmap = Bitmap.createBitmap(
				bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		return rotatedBitmap;
	}
	
	public int getRotationDegrees() {
		return rotationDegrees;
	}
}