package com.luciocossio.gestures.accelerometer;

import java.util.LinkedList;

import com.luciocossio.dsp.filters.FIRHighPassHalfBand;
import com.luciocossio.gestures.Gestures;
import com.luciocossio.gestures.TimeIntervalControl;

public class RightAndLeftShake {
	
	private boolean happening = false;	
	public LinkedList<Float> oldXs = new LinkedList<Float>();
	
	public float lastXValue = 0;
	public float lastFilteredXValue = 0;
	
	private TimeIntervalControl intervalControl = new TimeIntervalControl(500);
	
	//high pass filter
	private FIRHighPassHalfBand filter = new FIRHighPassHalfBand();
	
	private String name = "";
	
	private float triggerMagnitude = 0.6f;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public RightAndLeftShake()
	{
		for(int i=0; i<10; i++)
		{
			oldXs.add(0.0f);
		}		
	}
	
	public boolean verifyGesture(float x, float y, float z)
	{	
		//keep last values
		oldXs.add(x);
		if(oldXs.size() > 10)
		{
			oldXs.remove();
		}
		
		//update filter for X axis
		x = filter.filter(x);	
		
		if (!intervalControl.hasIntervalPassed()) return false;
			
		//Detect when started, a quick change in direction occurs
		if(Math.abs(x) > triggerMagnitude && !happening)
		{
			happening = true;				
			lastFilteredXValue = x;
			
			if(oldXs.get(9) > 0)
			{				
				setName(Gestures.SHAKE_LEFT);
			}
			else
			{				
				setName(Gestures.SHAKE_RIGHT);
			}
			
			intervalControl.triggerIt();
						
			return true;
		}
		else
		{
			happening = false;
		}
		
		return false;
	}

}
