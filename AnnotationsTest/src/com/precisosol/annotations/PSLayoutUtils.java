package com.precisosol.annotations;

import android.content.Context;
import android.util.TypedValue;

public class PSLayoutUtils {
	public static int getPixelAdjustedValue(Context ctx, int val){
		int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                (float)val , ctx.getResources().getDisplayMetrics());
		return value;
	}
}
