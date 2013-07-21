package com.luciocossio.classclient.listeners.impl;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.gestures.FlingDirectionIdentifier;
import com.luciocossio.gestures.Gestures;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class FlingTouchPresentationListener extends GestureDetector.SimpleOnGestureListener {
	
	FlingDirectionIdentifier directionIdentifier;
	PresentationClient client;
	ProgressDialog dialog;
	Activity activity;
	
	protected int scrollBoundary = 200;
	boolean presentationStarted = false;
	boolean callServer = true;

	public FlingTouchPresentationListener(PresentationClient client, ProgressDialog dialog, Activity activity)
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
    	
    	if(callServer && !presentationStarted && canStart())
    	{
    		if(side == Gestures.FLING_UP)
    		{
    			startPresentation(client, dialog);	
    		}
    	}
    	
    	if(presentationStarted && canClose())
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
    
    public abstract boolean canClose();
    
    public abstract boolean canStart();
    
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
