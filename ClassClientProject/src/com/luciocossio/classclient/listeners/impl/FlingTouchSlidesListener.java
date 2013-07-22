package com.luciocossio.classclient.listeners.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.ImageGalleryActivity.ImagePagerAdapter;
import com.luciocossio.classclient.activities.image.views.PresentationImageView;
import com.luciocossio.classclient.async.PresentationAsyncTask;

public class FlingTouchSlidesListener extends FlingTouchPresentationListener {	

	ViewPager viewPager;	
		
	public FlingTouchSlidesListener(PresentationClient client,
			ProgressDialog dialog, Activity activity, ViewPager viewPager) {
		super(client, dialog, activity);
		this.viewPager = viewPager;
	}	
	
	public void updatePositionY(float lastY, float currentY)
	{
		float localDistanceY = currentY - lastY;
		
		{
			if(viewPager.getScrollY()+localDistanceY < scrollBoundary && viewPager.getScrollY()+localDistanceY > -scrollBoundary)
			{
				viewPager.scrollTo( viewPager.getScrollX(), viewPager.getScrollY()+(int)localDistanceY );
			}
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(Math.abs(distanceX) > Math.abs(distanceY)) return false;
		
		this.updatePositionY(viewPager.getScrollY(), viewPager.getScrollY()+distanceY);
		return true;
	}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {

		if(!presentationStarted)
		{
			startPresentation(client, dialog);
		}
		
		return super.onSingleTapConfirmed(e);
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
    	viewPager.scrollTo(viewPager.getScrollX(), 0);
    }

	@Override
	protected boolean isFlingUp() {
		if(viewPager.getScrollY() > scrollBoundary - scrollBoundary/4) return true;
		return false;
	}

	@Override
	protected boolean isFlingDown() {
		if(viewPager.getScrollY() < -( scrollBoundary - scrollBoundary/4 )) return true;
		return false;
	}

}
