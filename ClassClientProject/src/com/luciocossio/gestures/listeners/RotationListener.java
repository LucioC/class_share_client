package com.luciocossio.gestures.listeners;

import android.util.Log;

import com.luciocossio.classclient.activities.image.views.ImageState;
import com.luciocossio.classclient.activities.image.views.TouchImageView;
import com.luciocossio.gestures.OnRotationGestureListener;

public class RotationListener implements OnRotationGestureListener {

	private float minimunAngleInterval;
	protected float accumulatedAngle = 0f;
	private float lastAngle = 0f;

	public RotationListener(float minimumAngleChange)
	{
		this.minimunAngleInterval = minimumAngleChange;
	}
	
	@Override
	public boolean onRotationStart() {
		accumulatedAngle = 0;
		lastAngle = 0;
		return true;
	}
	
	public boolean onRotationStop()
	{
		float difference = accumulatedAngle - lastAngle;		
		if(Math.abs(difference) >= minimunAngleInterval)
		{
			minimumRotationEvent(difference, accumulatedAngle, lastAngle);
			lastAngle = accumulatedAngle;
			Log.d("ROTATION", "Accumulated angle " + accumulatedAngle);
		}
		return true;
	}
	
	@Override
	public boolean onRotation(float angleChange, float distanceChange) {
		
		//if(Math.abs(distanceChange) > Math.abs(angleChange)) return false;
		
		accumulatedAngle += angleChange;		
		
		return true;
	}
	
	protected void minimumRotationEvent(float angleChange, float accumulatedAngle, float lastAngle)
	{
	
	}
	
}
