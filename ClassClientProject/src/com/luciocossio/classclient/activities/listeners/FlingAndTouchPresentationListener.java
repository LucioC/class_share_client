package com.luciocossio.classclient.activities.listeners;

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

public class FlingAndTouchPresentationListener extends GestureDetector.SimpleOnGestureListener {
	
	FlingDirectionIdentifier directionIdentifier;
	PresentationClient client;
	ProgressDialog dialog;
	Activity activity;
	ViewPager viewPager;
	
	protected int scrollBoundary = 200;
	boolean presentationStarted = false;
	boolean callServer = true;

	public FlingAndTouchPresentationListener(PresentationClient client, ProgressDialog dialog, Activity activity, ViewPager viewPager)
	{
		super();		
		directionIdentifier = new FlingDirectionIdentifier();
		this.client = client;
		this.dialog = dialog;
		this.activity = activity;
		this.viewPager = viewPager;
	}	
	
	protected float lastLocalDistanceY=0;
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
		float localDistanceY = e1.getY() - e2.getY();		
		float dy = localDistanceY - lastLocalDistanceY;
		
		if(Math.abs(dy) > 10 && Math.abs(viewPager.getScrollY()+dy) < scrollBoundary)
		{
			this.viewPager.scrollTo(this.viewPager.getScrollX(), (int)(this.viewPager.getScrollY()+dy) );
		}
		
		return true;
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
    
    public void startPresentation(PresentationClient client, ProgressDialog dialog)
    {
    	PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{				
				return client.startPresentation();
			}
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					presentationStarted = true;
					started();
				}
			}
		};
		task.execute();		
    }
    
    public void closePresentation(PresentationClient client, ProgressDialog dialog)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.closePresentation();
			}
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					closed();
				}
			}
		};
		task.execute();
	}	
    
    public void started()
    {
    	
    }
    
    public void closed()
    {
    	
    }
    
    protected void resetScrollHeight()
    {
    	this.viewPager.scrollTo(this.viewPager.getScrollX(), 0 );
    	lastLocalDistanceY = 0;    
    }
    
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
