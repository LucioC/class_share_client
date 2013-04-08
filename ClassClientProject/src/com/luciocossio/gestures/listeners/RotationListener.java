package com.luciocossio.gestures.listeners;

import android.util.Log;

import com.luciocossio.gestures.OnRotationGestureListener;

public class RotationListener implements OnRotationGestureListener {

	private float minimunAngleInterval;
	private float accumulatedAngle = 0f;
	private float lastAngle = 0f;
	
	public RotationListener(float minimumAngleChange)
	{
		this.minimunAngleInterval = minimumAngleChange;
	}
	
	@Override
	public boolean onRotationStart() {
		accumulatedAngle = 0;		
		return true;
	}

	@Override
	public boolean onRotation(float angleChange) {
		accumulatedAngle += angleChange;
		
		float difference = accumulatedAngle - lastAngle;		
		if(Math.abs(difference) > minimunAngleInterval)
		{
			rotationChange(difference, accumulatedAngle, lastAngle);
			lastAngle = accumulatedAngle;
			Log.d("ROTATION", "Accumulated angle " + accumulatedAngle);
		}
		
		return true;
	}
	
	protected void rotationChange(float angleChange, float accumulatedAngle, float lastAngle)
	{
	
	}
	
}
