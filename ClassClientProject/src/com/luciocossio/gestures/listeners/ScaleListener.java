package com.luciocossio.gestures.listeners;

import android.view.ScaleGestureDetector;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {	

	private float scaleFactor = 1.f;
	private float lastScaleFactor = 1f;
	
	private float scaleInterval;
	
	public ScaleListener(float scaleMinimunIntervalDifference)
	{
		this.scaleInterval = scaleMinimunIntervalDifference;
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		scaleFactor *= detector.getScaleFactor();

		// Don't let the object get too small or too large.
		scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
        
        float scaleDifference = lastScaleFactor - scaleFactor;
        
        //Trigger event only if there was a difference on the scale
        if(Math.abs(scaleDifference) >= scaleInterval)
        {
        	scaleChanged(scaleDifference, scaleFactor, lastScaleFactor);
        	lastScaleFactor = scaleFactor;
        }

		return true;
	}
	
	protected void scaleChanged(float scaleChange, float newScale, float lastScale)
	{
		
	}

	public float getScaleFactor() {
		return scaleFactor;
	}
}	