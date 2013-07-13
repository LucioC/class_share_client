package com.luciocossio.classclient.activities;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.R;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.classclient.async.PresentationAsyncTask;
import com.luciocossio.classclient.http.RESTApacheClient;
import com.luciocossio.classclient.http.RESTJsonClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class ControlImagePresentationActivity extends Activity {
	
	private PresentationClient client;	
	private ProgressDialog dialog;
	private String serverUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control_image_presentation);
		
		Intent intent = getIntent();
		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);		
		Log.i("ControlPowerPointActivity", "server url received:" + serverUrl);
		dialog = new ProgressDialog(this);
		
		initializePresentationClient();
	}	

	private void initializePresentationClient()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		client = new PresentationClient(jsonClient, serverUrl);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_control_image_presentation,
				menu);
		return true;
	}
	
	public void moveUp(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.moveImageUp();
			}			
		};
		task.execute();
	}
	
	public void moveDown(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.moveImageDown();
			}			
		};
		task.execute();
	}
	
	public void moveRight(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.moveImageRight();
			}			
		};
		task.execute();
	}
	
	public void moveLeft(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.moveImageLeft();
			}			
		};
		task.execute();
	}
	
	public void rotateRight(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.rotateImageRight();
			}			
		};
		task.execute();
	}
	
	public void rotateLeft(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.rotateImageLeft();
			}			
		};
		task.execute();
	}
	
	public void zoomIn(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.zoomInImage();
			}			
		};
		task.execute();
	}
	
	public void zoomOut(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.zoomOutImage();
			}			
		};
		task.execute();
	}	
	
	public void closeImage(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage executeTask()
			{
				return client.closeImage();
				//return null;
			}
			
			@Override
			protected void OnEndPostExecute(ResultMessage result)
			{
				if(result.getWasSuccessful())
				{
					thisPanel.finish();
				}
			}
		};
		task.execute();
	}	

}
