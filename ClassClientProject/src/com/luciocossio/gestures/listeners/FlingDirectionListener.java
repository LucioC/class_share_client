package com.luciocossio.gestures.listeners;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.luciocossio.gestures.FlingDirectionIdentifier;

public class FlingDirectionListener extends GestureDetector.SimpleOnGestureListener {
	
    private FlingDirectionIdentifier flingDirection = new FlingDirectionIdentifier();
    
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {
        
    	String side = flingDirection.onFlingReturnDirection(event1, event2);
        Log.d("FLING", "FlingDirection: " + side );
        flingOccured(side);
        
        return true;
    }
    
    protected void flingOccured(String side)
    {
    	
    }
}
