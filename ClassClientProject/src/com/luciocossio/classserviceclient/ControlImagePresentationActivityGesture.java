package com.luciocossio.classserviceclient;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.gestures.detectors.RotationGestureDetector;
import com.luciocossio.gestures.listeners.FlingAndMoveDirectionListener;
import com.luciocossio.gestures.listeners.RotationListener;
import com.luciocossio.gestures.listeners.ScaleListener;
import com.luciocossio.gestures.listeners.ShakeGestureListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;

public class ControlImagePresentationActivityGesture extends Activity   {
	
	private PresentationClient client;	
	private ProgressDialog dialog;
	private String serverUrl;
	
	private GestureDetectorCompat simpleGesturesDetector; 
	private ScaleGestureDetector scaleDetector;
	private RotationGestureDetector rotationDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control_image_presentation_gestures);
		
		Intent intent = getIntent();
		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);		
		Log.i("ControlPowerPointActivity", "server url received:" + serverUrl);
		dialog = new ProgressDialog(this);
		
		RotationListener rotationListener = new RotationListener(5f) 
		{			
			@Override
			protected void rotationChange(float angleChange, float accumulatedAngle, float lastAngle)
			{
		        Log.d("ROTATION", "Do something here for rotation with angle change: " + angleChange + " and accumulated " + accumulatedAngle );				
			}
		};
		rotationDetector = new RotationGestureDetector(rotationListener);
				
		FlingAndMoveDirectionListener flingListener = new FlingAndMoveDirectionListener(40f)
		{
			@Override
			protected void flingOccured(String side)
			{
		        Log.d("FLING", "Do something here for: " + side );				
			}
			
			@Override
			protected void moveChangeOccured(float changeInX, float changeInY)
		    {
		        Log.d("MOVE", "Do something here for x " + changeInX + " and y " + changeInY );		
		    }
			
		};		
		simpleGesturesDetector = new GestureDetectorCompat(this, flingListener);
		simpleGesturesDetector.setIsLongpressEnabled(false);
		
		ScaleListener scaleListener = new ScaleListener(.4f)
		{
			@Override
			protected void scaleChanged(float scaleChange, float newScaleFactor, float oldScaleFactor)
			{
				Log.d("SCALE", "Do something here for scale. changed from " + oldScaleFactor + " to " + newScaleFactor);				
			}
		};
	    scaleDetector = new ScaleGestureDetector(this, scaleListener);

	    ShakeGestureListener accelerometerListener = new ShakeGestureListener() 
	    {
	    	@Override
	    	protected void gestureTrigged(String gesture)
	    	{
	    		Log.d("GESTURE", "do something here for " + gesture);	
	    	}	    
	    };
	    SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    accelerometerListener.registerAccelerometerListener(sensorManager);	
	    
		initializePresentationClient();
	}	
	
	@Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.simpleGesturesDetector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        rotationDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
			protected ResultMessage ExecuteTask()
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
