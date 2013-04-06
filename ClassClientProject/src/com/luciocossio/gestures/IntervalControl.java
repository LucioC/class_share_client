package com.luciocossio.gestures;

public class IntervalControl {
	
	private long lastTriggedTime = 0;
	
	private long interval;
	
	public IntervalControl(long interval)
	{
		this.interval = interval;		
	}
	
	public void triggerIt()
	{
		lastTriggedTime = System.currentTimeMillis();		
	}
	
	public boolean hasIntervalPassed()
	{
		long now = System.currentTimeMillis();
		long difference = now - lastTriggedTime;
		
		if(difference > interval)
		{
			return true;
		}
		
		return false;
	}

}
