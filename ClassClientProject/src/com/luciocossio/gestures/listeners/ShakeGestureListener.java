package com.luciocossio.gestures.listeners;

import android.util.Log;

import com.luciocossio.gestures.accelerometer.RightAndLeftShake;

public class ShakeGestureListener extends AccelerometerListener {
	
	private RightAndLeftShake shakeGesture = new RightAndLeftShake();
	protected void afterAccelerationUpdated()
	{
		boolean shaked = shakeGesture.verifyGesture(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
		if(shaked)
		{
			Log.i("Side", shakeGesture.getName());
			gestureTrigged(shakeGesture.getName());
		}
	}
		
	protected void gestureTrigged(String gesture)
	{
		
	}

}
