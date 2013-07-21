package com.luciocossio.classclient.listeners.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;

public class FlingTouchSlidesListener extends FlingTouchPresentationListener {	

	ViewPager viewPager;
	
	public FlingTouchSlidesListener(PresentationClient client,
			ProgressDialog dialog, Activity activity, ViewPager viewPager) {
		super(client, dialog, activity);
		this.viewPager = viewPager;
	}	
	
	public void updatePositionY(PointF last, PointF current)
	{
		float localDistanceY = last.y - current.y;

		if(Math.abs(viewPager.getScrollY()+localDistanceY) < scrollBoundary)
		{
			this.viewPager.scrollTo(this.viewPager.getScrollX(), (int)(this.viewPager.getScrollY()+localDistanceY) );
		}		
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
			protected void onEndPostExecute(ResultMessage result)
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
			protected void onEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					closed();
				}
			}
		};
		task.execute();
	}
    
    public boolean canClose() 
    {
    	return true;
    }
    
    public boolean canStart() 
    {
    	return true;
    }
    
    protected void resetScrollHeight()
    {
    	this.viewPager.scrollTo(this.viewPager.getScrollX(), 0 );
    }

}
