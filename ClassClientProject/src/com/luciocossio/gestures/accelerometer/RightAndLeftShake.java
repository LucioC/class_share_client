package com.luciocossio.gestures.accelerometer;

import java.util.LinkedList;

import com.luciocossio.dsp.filters.FIRHighPassHalfBand;
import com.luciocossio.gestures.IntervalControl;

public class RightAndLeftShake {
	
	private boolean happening = false;	
	public LinkedList<Float> oldXs = new LinkedList<Float>();
	
	public float lastXValue = 0;
	public float lastFilteredXValue = 0;
	
	private IntervalControl intervalControl = new IntervalControl(500);
	
	//high pass filter
	private FIRHighPassHalfBand filter = new FIRHighPassHalfBand();
	
	public String name = "";
	
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
		if(Math.abs(x) > 1.0f && !happening)
		{
			happening = true;				
			lastFilteredXValue = x;
			
			if(oldXs.get(9) > 0)
			{				
				name = "LEFT";
			}
			else
			{				
				name = "RIGHT";
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
