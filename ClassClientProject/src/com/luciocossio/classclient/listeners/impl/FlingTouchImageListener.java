package com.luciocossio.classclient.listeners.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.image.views.TouchImageView;
import com.luciocossio.classclient.async.PresentationAsyncTask;

public class FlingTouchImageListener extends FlingTouchPresentationListener {
	
	TouchImageView imageView;
	String imageName = "";
	
	protected float yInitialPosition = 0;
	
	public FlingTouchImageListener(PresentationClient client,
			ProgressDialog dialog, Activity activity, TouchImageView imageView) {
		super(client, dialog, activity);
		this.imageView = imageView;
	}	
	
	public void updatePositionY(PointF last, PointF current)
	{
		float localDistanceY = last.y - current.y;

		if(Math.abs(imageView.getImagePoint().y-localDistanceY-imageView.getImageCenterPoint().y) < scrollBoundary)
		{
			//this.viewPager.scrollTo(this.viewPager.getScrollX(), (int)(this.viewPager.getScrollY()+dy) );
			PointF newPosition = new PointF(imageView.getImagePoint().x,imageView.getImagePoint().y-localDistanceY);
			this.imageView.updateImagePosition(newPosition, imageView.getImagePoint());
		}		
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		//return super.onSingleTapConfirmed(e);

		if(!presentationStarted)
		{
			startPresentation(client, dialog);
			return true;
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
				return client.openImage(imageName);
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
				return client.closeImage();
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
    	if(imageView.getImageScale() <= 1)
    	{
    		return true;    	
    	}
    	return false;
    }    
    
    public boolean canStart()
    {
    	if(imageView.getImageScale() <= 1)
    	{
    		return true;    	
    	}
    	return false;
    }  

    protected void resetScrollHeight()
    {
    	if(imageView.getImageScale() <= 1)
    	{
    		this.imageView.updateImagePosition(imageView.getImageCenterPoint(), imageView.getImagePoint());
    	}
    }

    public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	protected boolean isFlingUp() {
		if(this.imageView.getImagePoint().y < -Math.abs(scrollBoundary - scrollBoundary/4) ) return true;
		return false;
	}

	@Override
	protected boolean isFlingDown() {
		if(this.imageView.getImagePoint().y > Math.abs(scrollBoundary - scrollBoundary/4) ) return true;
		return false;
	}


}
