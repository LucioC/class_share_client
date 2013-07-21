package com.luciocossio.classclient.listeners.impl;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.gestures.FlingDirectionIdentifier;
import com.luciocossio.gestures.Gestures;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class FlingAndTouchPresentationListener extends GestureDetector.SimpleOnGestureListener {
	
	FlingDirectionIdentifier directionIdentifier;
	PresentationClient client;
	ProgressDialog dialog;
	Activity activity;
	
	protected int scrollBoundary = 200;
	boolean presentationStarted = false;
	boolean callServer = true;

	public FlingAndTouchPresentationListener(PresentationClient client, ProgressDialog dialog, Activity activity)
	{
		super();		
		directionIdentifier = new FlingDirectionIdentifier();
		this.client = client;
		this.dialog = dialog;
		this.activity = activity;
	}	

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if(presentationStarted)
		{
			activity.openOptionsMenu();
		}
		return super.onSingleTapConfirmed(e);
	}
	
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {

    	String side = directionIdentifier.onFlingReturnDirection(event1, event2);
    	float movementDistance = directionIdentifier.totalDistance(event1, event2);
    	
    	resetScrollHeight();
    	if( movementDistance < scrollBoundary - scrollBoundary/4) return true;
    	
    	if(callServer)
    	{
    		if(side == Gestures.FLING_UP)
    		{
    			startPresentation(client, dialog);    		
    		}
    	}
    	
    	if(presentationStarted)
    	{
    		if(side == Gestures.FLING_DOWN)
    		{
    			closePresentation(client, dialog);
    		}
    	}
        
        return true;
    }
    
    public abstract void startPresentation(PresentationClient client, ProgressDialog dialog);
    
    public abstract void closePresentation(PresentationClient client, ProgressDialog dialog);
    
    public void started()
    {
    	
    }
    
    public void closed()
    {
    	
    }
    
    protected abstract void resetScrollHeight();
    
    public void onFingerUp()
    {	
    	resetScrollHeight();
    }
	
	public void setPresentationStarted(boolean presentationStarted)
	{
		this.presentationStarted = presentationStarted;		
	}
	
	public void setCallServer(boolean callServer) {
		this.callServer = callServer;
	}

}
