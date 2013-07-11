package com.luciocossio.classclient.activities.listeners;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.PresentationAsyncTask;
import com.luciocossio.gestures.FlingDirectionIdentifier;
import com.luciocossio.gestures.Gestures;

import android.app.ProgressDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class FlingPresentationListener extends GestureDetector.SimpleOnGestureListener {
	
	FlingDirectionIdentifier directionIdentifier;
	PresentationClient client;
	ProgressDialog dialog;
	
	boolean callServer = true;
	
	public FlingPresentationListener(PresentationClient client, ProgressDialog dialog)
	{
		super();		
		directionIdentifier = new FlingDirectionIdentifier();
		this.client = client;
		this.dialog = dialog;
	}	

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {

    	String side = directionIdentifier.onFlingReturnDirection(event1, event2);

    	if(callServer)
    	{
    		if(side == Gestures.FLING_UP)
    		{
    			startPresentation(client, dialog);    		
    		}
    		else if(side == Gestures.FLING_DOWN)
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
			protected ResultMessage ExecuteTask()
			{
				return client.startPresentation();
			}
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
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
			protected ResultMessage ExecuteTask()
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
	
	public void setCallServer(boolean callServer)
	{
		this.callServer = callServer;		
	}

}
