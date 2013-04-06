
#include "dsk6713_aic23.h"			
Uint32 fs=DSK6713_AIC23_FREQ_8KHZ;	
short input, output;
short buffer_in[4000];
int i=0,j; 

long double cNum[11] = {0.00678340108999,   0.00658988696409,   0.02131807007402,  0.02416146895308,
   0.03454949009636,   0.03455629242850,   0.03454949009636,   0.02416146895308,
   0.02131807007402,   0.00658988696409,   0.00678340108999};

long double cDen[11] = {1.00000000000000,  -3.27912780384296,   6.05200458826077,  -7.12495721379462,
   5.98295040060413,  -3.59946497327471,   1.59018097320677,  -0.49383255743538,
   0.10614208361331,  -0.01339611720871,   0.00086154665495 };

  int oI[11];
  long double oO[11];
  long double y;

interrupt void c_int11()	           
{
 input = input_sample(); 	

 j=0;
 y=0;
	
	for(j=10;j>0;j--)
	{	
		oI[j] = oI[j-1];
		y += cNum[j]*oI[j];

		oO[j] = oO[j-1];
		y -= cDen[j]*oO[j];
	}

	oI[0] = input;
	y += cNum[0]*oI[0];

 oO[0] = y;
 output = (int)y;
 output_sample(output);  	           
 buffer_in[i]=input;

 i++;
 if (i>2000) i=0; 
}

main()
{
 comm_intr();				     
 while(1);              		     
}
