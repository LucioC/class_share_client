package com.luciocossio.classclient.activities.image;

import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PresentationSlidesActivity extends ImageGalleryActivity {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
