package com.luciocossio.classserviceclient;

import com.luciocossio.classclient.PresentationClient;
import com.luciocossio.classclient.RESTApacheClient;
import com.luciocossio.classclient.RESTJsonClient;
import com.luciocossio.classclient.ResultMessage;
import com.luciocossio.gestures.FlingDirection;
import com.luciocossio.gestures.accelerometer.RightAndLeftShake;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;

public class ControlImagePresentationActivityGesture extends Activity implements SensorEventListener  {
	
	private PresentationClient client;	
	private ProgressDialog dialog;
	private String serverUrl;
	private SensorManager sensorManager;
	
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
		
		registerAccelerometerListener();
		
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
	
	private void registerAccelerometerListener()
	{
		sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		// add listener. The listener will be HelloAndroid (this) class
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);	
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
	
	private float gravity[] = new float[3];
	private float linear_acceleration[] = new float[3];
	public void onSensorChanged(SensorEvent event){

		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			updateLinearAcceleration(event);
			
		}
	}
	
	private RightAndLeftShake shakeGesture = new RightAndLeftShake();
	protected void updateLinearAcceleration(SensorEvent event)
	{

		//CODE FROM ANDROID DEVELOPERS PAGE TO ELIMINATE GRAVITY EFFECT
		// In this example, alpha is calculated as t / (t + dT),
		// where t is the low-pass filter's time-constant and
		// dT is the event delivery rate.
		final float alpha = 0.8f;

		// Isolate the force of gravity with the low-pass filter.
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		// Remove the gravity contribution with the high-pass filter.
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];
		linear_acceleration[2] = event.values[2] - gravity[2];

		// assign directions
		//float x=linear_acceleration[0];
		//float y=linear_acceleration[1];
		//float z=linear_acceleration[2];	
		
		boolean shaked = shakeGesture.verifyGesture(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
		if(shaked)
		{
			Log.i("Filtered", Float.toString(shakeGesture.lastFilteredXValue) );
			Log.i("Not filtered", Float.toString(shakeGesture.lastXValue) );
			Log.i("Last values", shakeGesture.oldXs.toString() );
			Log.i("Side", shakeGesture.name);
		}
	}
	
	public void onAccuracyChanged(Sensor sensor,int accuracy){

	}

}
