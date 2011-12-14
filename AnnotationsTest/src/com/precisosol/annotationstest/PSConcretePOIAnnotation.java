package com.precisosol.annotationstest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.precisosol.annotations.PSLayoutUtils;
import com.precisosol.annotations.PSPOIAnnotation;
import com.precisosol.annotations.PSWayPoint;
import com.precisosol.annotations.R;
/*
 * PSPOIAnnotation is responsible for creating and returning annotation view for mapview and handles associated actions/animations
 */
public class PSConcretePOIAnnotation extends PSPOIAnnotation{

	public PSConcretePOIAnnotation(Context ctx, PSWayPoint poi){
		super(ctx,poi);
	}
	public ViewGroup getContainerView(){
		RelativeLayout layout = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(PSLayoutUtils.getPixelAdjustedValue(context, 40), PSLayoutUtils.getPixelAdjustedValue(context, 70));
		layout.setLayoutParams(params);
		return layout;
	}
	public View getPinView(){
		ImageView bg = new ImageView(context);
		bg.setBackgroundDrawable(null);
		bg.setImageDrawable(context.getResources().getDrawable(R.drawable.redpin));
		bg.setPadding(PSLayoutUtils.getPixelAdjustedValue(context, 0), PSLayoutUtils.getPixelAdjustedValue(context, 43), 0, 0);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_BASELINE);
		bg.setLayoutParams(params);
		return bg;
	}
	public View getPinStandView(){
		ImageView bg = new ImageView(context);
		bg.setImageDrawable(context.getResources().getDrawable(R.drawable.pinstand));
		bg.setBackgroundDrawable(null);
		bg.setPadding(PSLayoutUtils.getPixelAdjustedValue(context, 7), PSLayoutUtils.getPixelAdjustedValue(context, 45), 0, 0);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_BASELINE);
		bg.setLayoutParams(params);
		return bg;
	}
	
	
	@Override
	public Animation getOpenAnimation() {
		Animation _animation = new TranslateAnimation(0, 0, 0, PSLayoutUtils.getPixelAdjustedValue(context,-15));
        _animation.setDuration(1000);
        _animation.setFillAfter(true);
        return null;
	}
	@Override
	public Animation getCloseAnimation() {
		Animation _animation = new TranslateAnimation(0, 0, PSLayoutUtils.getPixelAdjustedValue(context,-15), PSLayoutUtils.getPixelAdjustedValue(context,0));
		_animation.setDuration(1000);
        _animation.setFillAfter(true);
		_animation.setRepeatMode(Animation.REVERSE);
		return null;
	}
}
