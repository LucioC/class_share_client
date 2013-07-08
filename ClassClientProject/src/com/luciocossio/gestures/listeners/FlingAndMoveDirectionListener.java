package com.luciocossio.gestures.listeners;

import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.luciocossio.gestures.FlingDirectionIdentifier;

public class FlingAndMoveDirectionListener extends GestureDetector.SimpleOnGestureListener {
	
    private FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
    private float minimunDistanceInterval;
    private PointF initialPoint;
    private PointF lastPoint;
    
    public FlingAndMoveDirectionListener(float minimunMoveDistanceInterval)
    {
    	this.minimunDistanceInterval = minimunMoveDistanceInterval;    	
    }
    
    @Override
    public boolean onDown(MotionEvent event) { 
    	initialPoint = new PointF(event.getX(), event.getY());
    	lastPoint = initialPoint;
    	
        //Log.d("DOWN", "onDown: " + event.toString()); 
        return true;
    }
    
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {
        
    	String side = flingDirection.onFlingReturnDirection(event1, event2);
        //Log.d("FLING", "FlingDirection: " + side );
        flingOccurred(side);
        
        return true;
    }    
	
	@Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {		
		
		PointF currentPoint = new PointF(e2.getX(), e2.getY());
		
		float distance = distanceBetweenPoints(lastPoint, currentPoint);
		if(distance >= minimunDistanceInterval)
		{
			moveChangeOccured(currentPoint.x - lastPoint.x, currentPoint.y - lastPoint.y);
			//Log.d("SCROLL", "onScroll: " + e1.toString()+e2.toString());     
			lastPoint = currentPoint;
		}
		
		return true;
    }
	
	private float distanceBetweenPoints(PointF initialPoint, PointF finalPoint)
	{
		float result = 0;
		
		float xDifference = (finalPoint.x - initialPoint.x);
		float yDifference = (finalPoint.y - initialPoint.y);
		result = xDifference*xDifference + yDifference*yDifference;
		result = (float) Math.sqrt(result);
		
		return result;		
	}
	
	protected void moveChangeOccured(float changeInX, float changeInY)
    {
    	
    }
    
    protected void flingOccurred(String side)
    {
    	
    }
}
