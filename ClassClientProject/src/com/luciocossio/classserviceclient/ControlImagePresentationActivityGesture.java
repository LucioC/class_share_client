package com.luciocossio.classserviceclient;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.gestures.AccelerometerListener;
import com.luciocossio.gestures.FlingDirection;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;

public class ControlImagePresentationActivityGesture extends Activity   {
	
	private PresentationClient client;	
	private ProgressDialog dialog;
	private String serverUrl;
	
	private GestureDetectorCompat detector; 
	private ScaleGestureDetector scaleDetector;
	private float scaleFactor = 1.f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control_image_presentation_gestures);
		
		Intent intent = getIntent();
		
		serverUrl = intent.getStringExtra(CommonVariables.ServerAddress);		
		Log.i("ControlPowerPointActivity", "server url received:" + serverUrl);
		dialog = new ProgressDialog(this);
		
		detector = new GestureDetectorCompat(this, new MyGestureListener());
	    scaleDetector = new ScaleGestureDetector(this, new ScaleListener());

	    AccelerometerListener accelerometerListener = new AccelerometerListener();
	    SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    accelerometerListener.registerAccelerometerListener(sensorManager);	
	    
		initializePresentationClient();
	}	
	
	@Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.detector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
	}

	private class ScaleListener 
	extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            Log.d("DEBUG", "Scaling: " + scaleFactor);

			//invalidate();
			return true;
		}
	}	

	private FlingDirection flingDirection = new FlingDirection();
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures"; 
        
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, 
                float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            
            Log.d(DEBUG_TAG, "FlingDirection: " + flingDirection.onFlingReturnDirection(event1, event2));
            return true;
        }
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                float distanceY) {
            Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
            Log.d(DEBUG_TAG, "onScrollDistances: " + distanceX + " : " + distanceY);
            return true;
        }
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
