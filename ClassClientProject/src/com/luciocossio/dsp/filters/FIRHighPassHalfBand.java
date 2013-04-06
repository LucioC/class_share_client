package com.luciocossio.dsp.filters;

public class FIRHighPassHalfBand {
	
	public float coef[] = {-0.025717f, 
	                       -0.000000f, 
	                       0.079549f, 
	                       -0.000000f, 
	                       -0.308753f, 
	                       0.500000f, 
	                       -0.308753f, 
	                       -0.000000f, 
	                       0.079549f, 
	                       -0.000000f, 
	                       -0.025717f};

	
	public float x[] = new float[11];
	public float y;
	
	public float filter(float input)
	{
		float output = 0;
		
		int j=0;
		y=0;
			
		for(j=10;j>0;j--)
		{	
			x[j] = x[j-1];
			y += coef[j]*x[j];
		}
		
		x[0] = input;
		y += coef[0]*x[0];
		
		output = y;
		return output;
	}
}
