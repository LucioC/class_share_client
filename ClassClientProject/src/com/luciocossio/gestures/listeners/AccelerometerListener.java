package com.luciocossio.gestures.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener implements SensorEventListener {
	
	public AccelerometerListener()
	{
		
	}
	
	public void registerAccelerometerListener(SensorManager sensorManager)
	{
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);	
	}
	
	private float gravity[] = new float[3];
	protected float linear_acceleration[] = new float[3];
	public void onSensorChanged(SensorEvent event){

		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			updateLinearAcceleration(event);	
			afterAccelerationUpdated();		
		}
	}
	
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
	}

	protected void afterAccelerationUpdated()
	{
		
	}
	
	public void onAccuracyChanged(Sensor sensor,int accuracy){

	}

}
