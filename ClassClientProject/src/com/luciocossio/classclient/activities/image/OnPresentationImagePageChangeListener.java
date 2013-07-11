package com.luciocossio.classclient.activities.image;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.PresentationAsyncTask;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class OnPresentationImagePageChangeListener implements OnPageChangeListener {
	
	private boolean callServer = false;
	private PresentationClient client;	
	private ProgressDialog dialog;
	
	public OnPresentationImagePageChangeListener(PresentationClient client, ProgressDialog dialog)
	{
		this.client = client;
		this.dialog = dialog;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {		
		/*Log.i("ChangeListener", "ScrollStateChanged " + arg0);

		switch(arg0)
		{
			case ViewPager.SCROLL_STATE_DRAGGING:
				Log.i("ChangeListener", "ScrollStateChanged dragging");
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				Log.i("ChangeListener", "ScrollStateChanged idle");
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				Log.i("ChangeListener", "ScrollStateChanged settling");
				break;
		} */
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		//Log.i("ChangeListener", "onPageScrolled offset" + arg1);
	}

	@Override
	public void onPageSelected(int position) {
		Log.i("ChangeListener", "onPageSelected " + position);

		if(callServer)
		{
			final String toSlide = String.valueOf(position+1);
			PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
			{
				@Override
				protected ResultMessage ExecuteTask()
				{
					return client.goToSlideNumber(toSlide);
				}
			};
			task.execute();
		}
	}
	
	public boolean isCallServerActive() {
		return callServer;
	}

	public void setCallServer(boolean callServer) {
		this.callServer = callServer;
	}

}
