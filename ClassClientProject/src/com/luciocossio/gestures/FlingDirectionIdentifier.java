package com.luciocossio.gestures;

import android.graphics.PointF;
import android.view.MotionEvent;

public class FlingDirectionIdentifier {
	
	public float totalDistance(MotionEvent event1, MotionEvent event2)
	{
		PointF point1 = new PointF(event1.getX(), event1.getY());
		PointF point2 = new PointF(event2.getX(), event2.getY());
		
		float distance = (float) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
		
		return distance;
	}
	
	public String onFlingReturnDirection(MotionEvent event1, MotionEvent event2)
	{
		float initialX = event1.getX();
		float finalX = event2.getX();
		
		float initialY = event1.getY();
		float finalY = event2.getY();
		
		float differenceX = finalX - initialX;		
		float differenceY = finalY - initialY;
		
		//Get the absolute difference between an horizontal or vertical movement
		boolean horizontal = (Math.abs(differenceX) > Math.abs(differenceY)) ? true : false;
		
		if(horizontal)
		{
			//If was a positive horizontal movement then is right, else left
			if( differenceX > 0)
			{
				return Gestures.FLING_RIGHT;
			}
			else
			{
				return Gestures.FLING_LEFT;
			}
		}
		else
		{
			//If was a positive vertical movement then is UP, else DOWN
			if(differenceY < 0)
			{
				return Gestures.FLING_UP;				
			}
			else
			{
				return Gestures.FLING_DOWN;				
			}			
		}
	}

}
