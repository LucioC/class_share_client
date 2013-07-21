package com.luciocossio.classclient.activities.image.views;

import com.luciocossio.classclient.listeners.impl.FlingTouchPresentationListener;
import com.luciocossio.classclient.listeners.impl.FlingTouchSlidesListener;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PresentationImageView extends ImageView {

	private GestureDetectorCompat simpleGesturesDetector; 
	private FlingTouchSlidesListener flingListener;
	
	public PresentationImageView(Context context, FlingTouchSlidesListener flingListener) {
		super(context);
		this.flingListener = flingListener;
		simpleGesturesDetector = new GestureDetectorCompat(getContext(), this.flingListener);
		registerListener();
	}
	
	private void registerListener()
    {
		setOnTouchListener(new OnTouchListener()
        {
			PointF last = null;
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	simpleGesturesDetector.onTouchEvent(event);
            	
            	int action = MotionEventCompat.getActionMasked(event);
                
        	    switch(action) {
        	        case MotionEvent.ACTION_DOWN:
						last = new PointF(event.getX(), event.getY());
						break;
						
					case MotionEvent.ACTION_UP:
						flingListener.onFingerUp();
						break;
						
					case MotionEvent.ACTION_MOVE:						
	                    float curX = event.getX();
	                    float curY = event.getY();
	                    PointF current = new PointF(curX, curY);
	                    flingListener.updatePositionY(last, current);
	                    last = current;
						break;
        	    }      
        	    
            	return true;
            }
        });
    }

}
