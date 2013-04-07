package com.luciocossio.gestures;

import android.view.MotionEvent;

public class FlingDirection {
	
	public static String UP = "UP";
	public static String DOWN = "DOWN";
	public static String RIGHT = "RIGHT";
	public static String LEFT = "LEFT";
	
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
				return RIGHT;
			}
			else
			{
				return LEFT;
			}			
		}
		else
		{
			//If was a positive vertical movement then is UP, else DOWN
			if(differenceY < 0)
			{
				return UP;				
			}
			else
			{
				return DOWN;				
			}			
		}
	}

}
