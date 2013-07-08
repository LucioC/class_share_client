package com.luciocossio.classserviceclient;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.gestures.Gestures;
import com.luciocossio.gestures.listeners.FlingAndMoveDirectionListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class ControlPowerPointActivityGesture extends Activity {
	
	private PresentationClient client;	
	private ProgressDialog dialog;
	private String serverUrl;

	private GestureDetectorCompat simpleGesturesDetector; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control_power_point_gestures);
		
		Intent intent = getIntent();		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);	
		
		Log.i("ControlPowerPointActivity", "server url received:" + serverUrl);
		dialog = new ProgressDialog(this);
		
		registerFlingGesture();
		
		initializePresentationClient();		
	}
	
	@Override 
    public boolean onTouchEvent(MotionEvent event){		
        this.simpleGesturesDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
	}
	
	public void registerFlingGesture()
	{
		FlingAndMoveDirectionListener flingListener = new FlingAndMoveDirectionListener(40f)
		{
			@Override
			protected void flingOccurred(String side)
			{
		        Log.d("FLING", "Fling occurred. Side: " + side );	
		        
		        if(side.equals(Gestures.FLING_RIGHT))
		        {
		        	goToNextSlide(null);
		        }
		        else if(side.equals(Gestures.FLING_LEFT))
		        {
		        	goToPreviosuSlide(null);
		        }
			}			
		};		
		simpleGesturesDetector = new GestureDetectorCompat(this, flingListener);
		simpleGesturesDetector.setIsLongpressEnabled(false);
	}

	private void initializePresentationClient()
	{
		RESTJsonClient jsonClient = new RESTApacheClient();
		client = new PresentationClient(jsonClient, serverUrl);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_control_power_point, menu);
		return true;
	}

	public void goToNextSlide(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				return client.nextSlide();
			}			
		};
		task.execute();
	}
	
	public void goToPreviosuSlide(View view)
	{
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				return client.previousSlide();
			}			
		};
		task.execute();
	}
	
	public void closePresentation(View view)
	{
		final Activity thisPanel = this;
		PresentationAsyncTask task = new PresentationAsyncTask(client, dialog)
		{
			@Override
			protected ResultMessage ExecuteTask()
			{
				return client.closePresentation();
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
