package com.luciocossio.classclient.activities.image;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.activities.CommonVariables;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.http.server.RegisterForServerUpdates;
import com.luciocossio.classclient.http.server.HTTPService;
import com.luciocossio.classclient.model.SlidePresentationInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

public class PresentationSlidesActivity extends ImageGalleryActivity {
		
	protected boolean runningState = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);        

        Intent intent = getIntent();
		runningState = intent.getBooleanExtra(CommonVariables.RunningState, runningState);	
		
		if(runningState)
		{
			this.flingListener.setPresentationStarted(true);
			this.pageListener.setCallServer(true);
			AsyncTask<String, Void, SlidePresentationInfo> task = createTaskPresentationInfo(
					this, client);
			this.getImages(true);
		}
		else			
		{
			this.getImages(false);
		}
        
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getPresentationInfoFromServer();
			}
		};
		RegisterForServerUpdates.startServiceForSlidesUpdate(this, client);
	}
	
	@Override
	public void updateStateOfImages()
	{
		getPresentationInfoFromServer();
	}
	
	public void getPresentationInfoFromServer()
	{			
		final PresentationSlidesActivity activity = this;
		final PresentationClient client = this.client;
		AsyncTask<String, Void, SlidePresentationInfo> task = createTaskPresentationInfo(
				activity, client);
		task.execute();
	}

	protected AsyncTask<String, Void, SlidePresentationInfo> createTaskPresentationInfo(
			final PresentationSlidesActivity activity,
			final PresentationClient client) {
		AsyncTask<String, Void, SlidePresentationInfo> task = new AsyncTask<String, Void, SlidePresentationInfo>()
		{
			SlidePresentationInfo result;

			@Override
			protected SlidePresentationInfo doInBackground(String... params) {						
				result = client.getSlidePresentationInfo();
				return result;
			}
			
			@Override
			protected void onPostExecute(SlidePresentationInfo result) {
				activity.updatePresentationState(result);
			}
			
			@Override
			protected void onPreExecute() {
			}
		};
		return task;
	}
	
	public void updatePresentationState(SlidePresentationInfo presentationInfo)
	{
		if(!presentationInfo.getFileName().equals(""))
		{
			if(viewPager.getCurrentItem() != presentationInfo.getCurrentSlide()-1)
			{
				pageListener.setToIgnoreNextPageChange();
				viewPager.setCurrentItem(presentationInfo.getCurrentSlide()-1);
			}
		}
		else
		{
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RegisterForServerUpdates.stopService(this, client);
	}
	
	private BroadcastReceiver receiver;
	@Override
	protected void onStart() {
	    super.onStart();
	    LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(HTTPService.NEW_REQUEST));
	}

	@Override
	protected void onStop() {
	    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	    super.onStop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_control_power_point, menu);
	    return true;
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		closePresentation();
	}
	
	protected void closePresentation()
	{
		final Activity activity = this;
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{			
			@Override
			protected ResultMessage executeTask() {
				return client.closePresentation();
			}
			
			@Override
			protected void onEndPostExecute(ResultMessage result) {
				super.onEndPostExecute(result);
				activity.finish();
			}
		};
		task.execute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_close:
	        	closePresentation();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
