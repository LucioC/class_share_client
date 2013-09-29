package com.luciocossio.gestures.detectors;

import com.luciocossio.gestures.OnRotationGestureListener;

import android.graphics.PointF;
import android.view.MotionEvent;

//Code adapted from here http://stackoverflow.com/questions/10682019/android-two-finger-rotation
//by Jorge Garcia
public class RotationGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	
	private PointF finger1, finger2, focalPoint;
	private int ptrID1, ptrID2;
	
	private float angleChange;
	private boolean firstTouch;
	private boolean active = false;

	private OnRotationGestureListener listener;

	public float getAngle() {
		return angleChange;
	}

	public RotationGestureDetector(OnRotationGestureListener listener){
		this.listener = listener;
		ptrID1 = INVALID_POINTER_ID;
		ptrID2 = INVALID_POINTER_ID;		
	}

	public boolean onTouchEvent(MotionEvent event){
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			finger1 = new PointF(event.getX(), event.getY());
			ptrID1 = event.getPointerId(0);
			angleChange = 0; 
			firstTouch = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			finger2 = new PointF(event.getX(), event.getY());			
			focalPoint = new PointF(getMidpoint(finger2.x, finger1.x), getMidpoint(finger2.y, finger1.y));
			ptrID2 = event.getPointerId(event.getActionIndex());
			angleChange = 0;
			firstTouch = true;
			break;
		case MotionEvent.ACTION_MOVE:

			if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
				PointF newFinger1, newFinger2;
				newFinger1 = new PointF(event.getX(event.findPointerIndex(ptrID1)), event.getY(event.findPointerIndex(ptrID1)));
				newFinger2 = new PointF(event.getX(event.findPointerIndex(ptrID2)), event.getY(event.findPointerIndex(ptrID2)));
				if (firstTouch) {
					angleChange = 0;
					firstTouch = false;
					active = true;
					listener.onRotationStart();
				} else {
					angleChange = angleBetweenLines(finger1, finger2, newFinger1, newFinger2);
				}
				
				float distance1 = distanceBetweenPoints(finger1,finger2);
				float distance2 = distanceBetweenPoints(newFinger1, newFinger2);

				if (listener != null) {
					listener.onRotation(angleChange, distance2 - distance1);
				}
				
				finger1 = newFinger1;
				finger2 = newFinger2;
			}
			break;
		case MotionEvent.ACTION_UP:
			ptrID1 = INVALID_POINTER_ID;
			if(active) listener.onRotationStop();
			active = false;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			ptrID2 = INVALID_POINTER_ID;
			if(active) listener.onRotationStop();
			active = false;
			break;
		}
		return true;
	}

	private float distanceBetweenPoints(PointF p1, PointF p2)
	{
		float distance = (float)Math.sqrt( Math.pow(p1.x-p2.x,2) + Math.pow(p1.y - p2.y,2));
		return distance;		
	}

	private float getMidpoint(float a, float b){
		return (a + b) / 2;
	}

	float findAngleDelta( float angle1, float angle2 )
	{
		float From = ClipAngleTo0_360( angle2 );
		float To   = ClipAngleTo0_360( angle1 );

		float Dist  = To - From;

		if ( Dist < -180.0f )
		{
			Dist += 360.0f;
		}
		else if ( Dist > 180.0f )
		{
			Dist -= 360.0f;
		}

		return Dist;
	}

	float ClipAngleTo0_360( float Angle ) { 
		return Angle % 360.0f; 
	}

	private float angleBetweenLines (PointF finger1, PointF finger2, PointF newFinger1, PointF newFinger2)
	{
		float angle1 = (float) Math.atan2( (finger1.y - finger2.y), (finger1.x - finger2.x) );
		float angle2 = (float) Math.atan2( (newFinger1.y - newFinger2.y), (newFinger1.x - newFinger2.x) );

		return findAngleDelta((float)Math.toDegrees(angle1),(float)Math.toDegrees(angle2));
	}
}
