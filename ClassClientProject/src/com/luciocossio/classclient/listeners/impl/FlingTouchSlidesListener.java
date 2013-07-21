package com.luciocossio.classclient.listeners.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;

public class FlingTouchSlidesListener extends FlingAndTouchPresentationListener {	

	ViewPager viewPager;
	
	public FlingTouchSlidesListener(PresentationClient client,
			ProgressDialog dialog, Activity activity, ViewPager viewPager) {
		super(client, dialog, activity);
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
    
    protected void resetScrollHeight()
    {
    	this.viewPager.scrollTo(this.viewPager.getScrollX(), 0 );
    	lastLocalDistanceY = 0;    
    }

}
