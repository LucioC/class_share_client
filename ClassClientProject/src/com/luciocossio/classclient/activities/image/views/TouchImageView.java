package com.luciocossio.classclient.activities.image.views;

import com.luciocossio.classclient.listeners.ImageStateConnector;
import com.luciocossio.classclient.model.ImagePresentationInfo;
import com.luciocossio.gestures.detectors.RotationGestureDetector;
import com.luciocossio.gestures.listeners.RotationListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.provider.MediaStore.Images;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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

	float screenWidth, screenHeight;
	static final int CLICK = 3;
	float saveScale = 1f;
	float right, bottom, origWidth, origHeight, bitmapWidth, bitmapHeight;
	
	ScaleGestureDetector mScaleDetector;
	
	private RotationGestureDetector rotationDetector;
	private RotationListener rotationListener;
	
	Context context;
	
	ScaleListener scaleListener = null;
	ImageStateConnector connector = null;
	
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
		scaleListener.setImageEventsConnection(connector);
		mScaleDetector = new ScaleGestureDetector(context, scaleListener);
		matrix.setTranslate(1f, 1f);
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);
		createRotationDetector();
		registerListener(null);
	}
	
	public void updateImageState(ImagePresentationInfo imageInfo)
	{
		ImageState currentState = getImageCurrentState();
		
		ImageState newState = new ImageState();
				
		int imageWidthToShow = imageInfo.getRight() - imageInfo.getLeft();		
		int imageHeightToShow = imageInfo.getBottom() - imageInfo.getTop();
		
		float heightMultiplier = (float)screenHeight / imageHeightToShow;
		float widthMultiplier = (float)screenWidth / imageWidthToShow;
				
		//set rotation
		newState.setRotation(imageInfo.getRotation());
		recreateWithRotation(newState.getRotation(), false);
		
		int x = 0;
		int y = 0;
		float zoom = currentState.getZoom();
		if(heightMultiplier < widthMultiplier)
		{
			zoom = heightMultiplier;
			newState.setZoom(zoom);
			newState.setTop((int)(imageInfo.getTop()*zoom));
			newState.setBottom((int)(imageInfo.getBottom()*zoom));

			int screenWidth = (int)this.screenWidth;
			int imageScaledWidthToShow = (int)(imageWidthToShow*zoom);
			int widthExcess = (int)(screenWidth - imageScaledWidthToShow);
			newState.setLeft((int)(imageInfo.getLeft()*zoom));
			newState.setRight((int)(imageInfo.getRight()*zoom));
			
			int imageScaledWidth = (int)(currentState.getWidth()*zoom);
			if(imageScaledWidth < screenWidth)
			{
				newState.setLeft(screenWidth/2 - imageScaledWidth/2);
				x = newState.getLeft();				
			}
			else
			{
				x = -newState.getLeft() + (widthExcess/2);				
			}
			y = -newState.getTop();
		}
		else
		{
			zoom = widthMultiplier;
			newState.setZoom(zoom);
			newState.setLeft((int)(imageInfo.getLeft()*zoom));
			newState.setRight((int)(imageInfo.getRight()*zoom));

			int screenHeight = (int)this.screenHeight;
			int imageScaledHeightToShow = (int)(imageHeightToShow*zoom);
			int heightExcess = (int)(screenHeight - imageScaledHeightToShow);			
			newState.setTop((int)(imageInfo.getTop()*zoom));
			newState.setBottom((int)(imageInfo.getBottom()*zoom));

			int imageScaledHeight = (int)(currentState.getHeight()*zoom);
			if(imageScaledHeight < screenHeight) 
			{
				newState.setTop(screenHeight/2 - imageScaledHeight/2);
				y = newState.getTop();
			}
			else
			{
				y = -newState.getTop() + (heightExcess/2);
			}
			x = -newState.getLeft();
		}

		float scaleFactor = newState.getZoom() / currentState.getZoom();
		scaleByMultiplier((int)screenWidth / 2, (int)screenHeight / 2, scaleFactor);
		updateImagePosition(new PointF(x, y), getImagePoint(), true);
		
		invalidate();
	}
	
	public void addRotation(float rotation)
	{
		//matrix = this.getImageMatrix();
		matrix.postRotate(rotation, (int)screenWidth / 2, (int)screenHeight / 2);
		setImageMatrix(matrix);
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
						if (mode == DRAG && getImageScale() > 1)
						{
							updateImagePositionAndTriggerServerUpdate(curr, last, false);
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
		final int minimumAngle = 15;
		rotationListener = new RotationListener(minimumAngle)
		{			
			@Override
			public boolean onRotationStart() {
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

				if(mode == ROTATION)
				{
					imageView.addRotation(-angleChange*4);
				}

				return super.onRotation(angleChange, distanceChange);				
			}
			
			@Override
			protected void minimumRotationEvent(float angleChange,
					float accumulatedAngle, float lastAngle) {
				
				float normalizedAngleChange = angleChange*4;
				while(normalizedAngleChange > 360) normalizedAngleChange -= 360;
				while(normalizedAngleChange < 0) normalizedAngleChange += 360;
				
				float leftOver = normalizedAngleChange % 90;
				normalizedAngleChange -= leftOver;
				if(leftOver > 45f) normalizedAngleChange += 90;
				
				imageView.recreateWithRotation(imageView.getRotationDegrees() - (int)normalizedAngleChange, true);							
			}	
		};
		rotationDetector = new RotationGestureDetector(rotationListener);		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(rotated || reset)
		{
			connector.setImageAngle(this.rotationDegrees);
			
			if(rotationDegrees == 90 || rotationDegrees == 270)
			{
				connector.updateVisiblePart(0, 0, imageBitmap.getHeight(), imageBitmap.getWidth(), imageBitmap.getWidth(), imageBitmap.getHeight());
			}
			else
			{
				connector.updateVisiblePart(0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getHeight(), imageBitmap.getWidth());
			}
			
			rotated = false;
			reset = false;
		}
	}
		
	private boolean rotated = false;
	private boolean reset = false;
	
	public void recreateWithRotation(int degrees, boolean shouldUpdateServer)
	{
		if(degrees != getRotationDegrees())
		{
			rotated = shouldUpdateServer;
			setImageBitmap(getImageBitmap(), degrees);
			invalidate();
		}
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

	public void updateImagePositionAndTriggerServerUpdate(PointF curr, PointF last) {
		updateImagePositionAndTriggerServerUpdate(curr, last, true);
	}	
	
	public void updateImagePositionAndTriggerServerUpdate(PointF curr, PointF last, boolean verticalMove) {
		
		if(mode != DRAG) return;
		
		updateImagePosition(curr, last, verticalMove);		

		updateVisiblePartDimensions();	
		
	}

	protected void updateImagePosition(PointF curr, PointF last,
			boolean verticalMove) {
		matrix.getValues(m);
		float x = m[Matrix.MTRANS_X];
		float y = m[Matrix.MTRANS_Y];
		float deltaX = curr.x - last.x;
		float deltaY = curr.y - last.y;
		float scaleWidth = Math.round(origWidth * saveScale);
		float scaleHeight = Math.round(origHeight * saveScale);
		if (scaleWidth < screenWidth)
		{
			deltaX = 0;
			if (y + deltaY > 0)
				deltaY = -y;
			else if (y + deltaY < -bottom)
				deltaY = -(y + bottom);
		}
		else if (scaleHeight < screenHeight)
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
		
		last.set(curr.x, curr.y);
		setImageMatrix(matrix);
	}			
	
	public void setListener(ImageStateConnector newListener)
	{
		this.connector = newListener;		
		scaleListener.setImageEventsConnection(connector);
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
			bitmapWidth = bm.getWidth();
			bitmapHeight = bm.getHeight();
		}
	}
	
	private int rotationDegrees = 0;

	public void setImageBitmap(Bitmap bm, int degrees)
	{
		setRotationDegrees(degrees);
		bm = copyBitmapWithRotation(bm, degrees);
		super.setImageBitmap(bm);
		if (bm != null)
		{
			bitmapWidth = bm.getWidth();
			bitmapHeight = bm.getHeight();
		}
	}
	
	public void setRotationDegrees(int degrees)
	{
		this.rotationDegrees = degrees;
		
		if(rotationDegrees > 360) rotationDegrees -= 360;
		if(rotationDegrees < 0) rotationDegrees += 360;
	}

	public void setMaxZoom(float x)
	{
		maxScale = x;	
	}
	
	protected void updateVisiblePartDimensions() {
		
		if( getDrawable() == null ) return;
		
		ImageState imageInfo = getImageCurrentState();
		
		connector.setImageAngle(this.rotationDegrees);

		connector.updateVisiblePart(imageInfo.getLeft(), imageInfo.getTop(), imageInfo.getRight(), imageInfo.getBottom(), imageInfo.getHeight(), imageInfo.getWidth());
	}

	public ImageState getImageCurrentState() {
		//get real view that is being showed on screen
		matrix.getValues(m);
		float left = m[Matrix.MTRANS_X];
		float top = m[Matrix.MTRANS_Y];
		if(top > 0) top = 0;
		float scale = m[Matrix.MSCALE_X];
		float right = -left + screenWidth;
		right = (right!=0) ? right/scale : 0;
		float bottom = -top + screenHeight;
		bottom = (bottom!=0) ? bottom/scale : 0;	
		if(bottom > bitmapHeight) bottom = bitmapHeight;

		left = (left!=0) ? left/scale : 0;
		top = (top!=0) ? top/scale : 0;
		
		ImageState imageInfo = new ImageState();
		imageInfo.setLeft((int)Math.abs(left));
		imageInfo.setRight((int)Math.abs(right));
		imageInfo.setTop((int)Math.abs(top));
		imageInfo.setBottom((int)Math.abs(bottom));
		imageInfo.setZoom(scale);
		imageInfo.setHeight(this.getDrawable().getBounds().height());
		imageInfo.setWidth(this.getDrawable().getBounds().width());
		return imageInfo;
	}

	private class ScaleListener extends
		ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		ImageStateConnector connector;
		
		public void setImageEventsConnection(ImageStateConnector listener)
		{
			this.connector = listener;
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
			scaleByMultiplier((int)detector.getFocusX(), (int)detector.getFocusY(), mScaleFactor);
			updateVisiblePartDimensions();
			return true;
		}		
	}
	
	public void scaleByMultiplier(int focusX, int focusY,
			float mScaleFactor) {
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
		right = screenWidth * saveScale - screenWidth - (2 * redundantXSpace * saveScale);
		bottom = screenHeight * saveScale - screenHeight
			- (2 * redundantYSpace * saveScale);
		if (origWidth * saveScale <= screenWidth || origHeight * saveScale <= screenHeight)
		{
			matrix.postScale(mScaleFactor, mScaleFactor, screenWidth / 2, screenHeight / 2);

			if (mScaleFactor < 1)
			{
				matrix.getValues(m);
				float x = m[Matrix.MTRANS_X];
				float y = m[Matrix.MTRANS_Y];
				if (mScaleFactor < 1)
				{
					if (Math.round(origWidth * saveScale) < screenWidth)
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
			matrix.postScale(mScaleFactor, mScaleFactor, focusX,
				focusY);
			
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
	}
	
	public float getImageScale()
	{
		return saveScale;		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		screenWidth = MeasureSpec.getSize(widthMeasureSpec);
		screenHeight = MeasureSpec.getSize(heightMeasureSpec);
		resizeAndCentralize();
	}
	
	public void resetZoom()
	{
		resizeAndCentralize();
		reset = true;
	}

	protected void resizeAndCentralize() {
		// Fit to screen.
		float scale;
		float scaleX = (float) screenWidth / (float) bitmapWidth;
		float scaleY = (float) screenHeight / (float) bitmapHeight;
		scale = Math.min(scaleX, scaleY);
		matrix.setScale(scale, scale);
		setImageMatrix(matrix);
		saveScale = 1f;

		// Center the image
		redundantYSpace = (float) screenHeight - (scale * (float) bitmapHeight);
		redundantXSpace = (float) screenWidth - (scale * (float) bitmapWidth);
		redundantYSpace /= (float) 2;
		redundantXSpace /= (float) 2;

		matrix.postTranslate(redundantXSpace, redundantYSpace);

		origWidth = screenWidth - 2 * redundantXSpace;
		origHeight = screenHeight - 2 * redundantYSpace;
		right = screenWidth * saveScale - screenWidth - (2 * redundantXSpace * saveScale);
		bottom = screenHeight * saveScale - screenHeight - (2 * redundantYSpace * saveScale);
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