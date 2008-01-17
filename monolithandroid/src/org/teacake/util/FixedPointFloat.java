package org.teacake.util;

public class FixedPointFloat {
	
	public static int floatToFixedPoint(float number)
	{
		java.math.BigDecimal bd = new java.math.BigDecimal(number);
		
			return Float.floatToIntBits(number)*256;
	}
	public static float fixedPointToFloat(int number)
	{
		float retval =0.0f;
		retval = retval - (number&(1<<31));
		int intpart = (number>>16)&0xffff;
		int fractpart = number&0xffff;
		for (int i=0;i<15;i++)
		{
			retval+=(1<<i)&intpart;
		}
		for (int i=0;i<16;i++)
		{
			retval+=(fractpart>>(15-i))*(1.0/(2>>i));
		}
		return retval;
		
	}
	
}
