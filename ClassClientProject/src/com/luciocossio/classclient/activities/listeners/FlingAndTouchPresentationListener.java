package com.luciocossio.classclient.activities.listeners;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.gestures.FlingDirectionIdentifier;
import com.luciocossio.gestures.Gestures;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class FlingAndTouchPresentationListener extends GestureDetector.SimpleOnGestureListener {
	
	FlingDirectionIdentifier directionIdentifier;
	PresentationClient client;
	ProgressDialog dialog;
	Activity activity;
	
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
    	
    	if(movementDistance < 250) return true;

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
	
	public void setPresentationStarted(boolean presentationStarted)
	{
		this.presentationStarted = presentationStarted;		
	}
	
	public void setCallServer(boolean callServer) {
		this.callServer = callServer;
	}

}
