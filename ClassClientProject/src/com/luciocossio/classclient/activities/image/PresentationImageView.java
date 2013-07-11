package com.luciocossio.classclient.activities.image;

import com.luciocossio.classclient.activities.listeners.FlingPresentationListener;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PresentationImageView extends ImageView {

	private GestureDetectorCompat simpleGesturesDetector; 
	private FlingPresentationListener flingListener;
	
	public PresentationImageView(Context context, FlingPresentationListener flingListener) {
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
            	return true;
            }
        });
    }

}
