package com.luciocossio.classclient.activities.image.views;

import com.luciocossio.classclient.activities.listeners.FlingAndTouchPresentationListener;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PresentationImageView extends ImageView {

	private GestureDetectorCompat simpleGesturesDetector; 
	private FlingAndTouchPresentationListener flingListener;
	
	public PresentationImageView(Context context, FlingAndTouchPresentationListener flingListener) {
		super(context);
		this.flingListener = flingListener;
		simpleGesturesDetector = new GestureDetectorCompat(getContext(), this.flingListener);
		registerListener();
	}
	
	private void registerListener()
    {
		setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	simpleGesturesDetector.onTouchEvent(event);
            	
            	int action = MotionEventCompat.getActionMasked(event);
                
        	    switch(action) {
        	        case (MotionEvent.ACTION_UP) :
        	            flingListener.onFingerUp();
        	            return true;
        	    }      
        	    
            	return true;
            }
        });
    }

}
