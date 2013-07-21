package com.luciocossio.classclient.listeners.impl;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.async.AsyncSilentTask;
import com.luciocossio.classclient.listeners.ImageMoveZoomPanListener;

public class ImageMoveZoomPanConnector implements ImageMoveZoomPanListener {
	
	long lastTime = 0;
	long minimumIntervalMillis = 10;
	
	protected float dleft = 0;
	protected float dtop = 0;
	protected float dright = 0;
	protected float dbottom = 0;

	protected float left = 0;
	protected float right = 0;
	protected float top = 0;
	protected float bottom = 0;
	
	protected int minimumMoveInterval = 25;
	
	PresentationClient client;
	
	public ImageMoveZoomPanConnector(PresentationClient client, int minimumMoveInterval, long minimumTimeIntervalInMillis)
	{
		this.minimumMoveInterval = minimumMoveInterval;
		this.minimumIntervalMillis = minimumTimeIntervalInMillis;
		this.client = client;
	}
	
	@Override
	public void updateVisiblePart(int left, int top, int right, int bottom, int imageHeight, int imageWidth) {
		dleft = left - this.left;
		dright = right - this.right;
		dtop = top - this.top;
		dbottom = bottom - this.bottom;
		
		if(System.currentTimeMillis() > lastTime + minimumIntervalMillis)
		if(Math.abs(dleft) >= minimumMoveInterval || Math.abs(dright) >= minimumMoveInterval 
				|| Math.abs(dtop) >= minimumMoveInterval || Math.abs(dbottom) >= minimumMoveInterval)
		{
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;

			final Integer newLeft = (int)left;
			final Integer newRight = (int)right;
			final Integer newTop = (int)top;
			final Integer newBottom = (int)bottom;
			final Integer sourceHeight = imageHeight;
			final Integer sourceWidth = imageWidth;
			
			lastTime = System.currentTimeMillis();
			AsyncSilentTask task = new AsyncSilentTask(client)
			{
				protected com.luciocossio.classclient.ResultMessage executeTask() {
					return this.client.updateImageVisiblePart(newLeft, newTop, newRight, newBottom, sourceHeight, sourceWidth);
				}
			};
			task.execute();
		}
	}

	@Override
	public void rotate(float angle) {
		// TODO Auto-generated method stub
		
	}
}
