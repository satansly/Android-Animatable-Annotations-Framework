package com.precisosol.annotationstest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.precisosol.annotations.PSLayoutUtils;
import com.precisosol.annotations.PSPOIAnnotation;
import com.precisosol.annotations.PSPOICallout;
import com.precisosol.annotations.PSWayPoint;
import com.precisosol.annotations.R;

public class PSConcretePOICallout extends PSPOICallout{
	public PSConcretePOICallout(Context ctx, PSPOIAnnotation ann){
		super(ctx,ann);
	}
	public ViewGroup getContainerView(){
		RelativeLayout layout = new RelativeLayout(context);
		layout.setPadding(0, 0, 0, PSLayoutUtils.getPixelAdjustedValue(context, 40));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params);
		return layout;
	}
	public View getBackgroundView(){
		ImageView bg = new ImageView(context);
		bg.setImageDrawable(context.getResources().getDrawable(R.drawable.calloutbg_iphone));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( PSLayoutUtils.getPixelAdjustedValue(context, 262), PSLayoutUtils.getPixelAdjustedValue(context, 85));
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bg.setLayoutParams(params);
		return bg;
	}
	public View getTitleView(){
		TextView vw = new TextView(context);
		vw.setLines(1);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( PSLayoutUtils.getPixelAdjustedValue(context, 200), LayoutParams.WRAP_CONTENT);
		params.topMargin = PSLayoutUtils.getPixelAdjustedValue(context, 18);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		vw.setLayoutParams(params);
		return vw;
	}
	
	public View getSubtitleView(){
		TextView vw = new TextView(context);
		vw.setTextSize(10);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( PSLayoutUtils.getPixelAdjustedValue(context, 200), LayoutParams.WRAP_CONTENT);
		if(title != null){
			params.addRule(RelativeLayout.ALIGN_LEFT, title.getId());
			params.addRule(RelativeLayout.BELOW, title.getId());
		}
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		vw.setLayoutParams(params);
		return vw;
	}
	public View getAccessoryView(){
		ImageButton vw = new ImageButton(context);
		vw.setBackgroundDrawable(null);
		vw.setImageDrawable(context.getResources().getDrawable(R.drawable.info));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( PSLayoutUtils.getPixelAdjustedValue(context, 45), PSLayoutUtils.getPixelAdjustedValue(context, 45));
		params.setMargins(PSLayoutUtils.getPixelAdjustedValue(context, 10), 0, 0, 0);
		if(title != null){
			params.addRule(RelativeLayout.RIGHT_OF,title.getId());
			params.addRule(RelativeLayout.ALIGN_TOP,title.getId());
		}
		vw.setLayoutParams(params);
		vw.setScaleType(ScaleType.FIT_CENTER);
		return vw;
	}
	@Override
	public Animation getOpenAnimation() {
		Animation _animation = new ScaleAnimation(0,  1,0, 1,Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.6);
		_animation.setDuration(500);
        _animation.setFillAfter(true);
		_animation.setRepeatMode(Animation.REVERSE);
		return _animation;
	}
	@Override
	public Animation getCloseAnimation() {
		Animation _animation = new ScaleAnimation(1,0, 1, 0,Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.6);
        _animation.setDuration(500);
        _animation.setFillAfter(true);
        
		return _animation;
	}
	}
