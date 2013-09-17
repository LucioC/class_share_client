package com.luciocossio.classclient.activities.image;

import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.http.server.RegisterForServerUpdates;
import com.luciocossio.classclient.http.server.HTTPService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

public class PresentationSlidesActivity extends ImageGalleryActivity {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				updateSlidesState();
			}
		};
		RegisterForServerUpdates.startServiceForSlidesUpdate(this, client);
	}
	
	public void updateSlidesState()
	{
		Log.i("SlidesAcitivity","slides state should be updated");
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
