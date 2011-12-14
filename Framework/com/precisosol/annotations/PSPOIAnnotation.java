package com.precisosol.annotations;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.precisosol.annotations.PSPOICallout.PSCalloutHandler;
/*
 * PSPOIAnnotation is responsible for creating and returning annotation view for mapview and handles associated actions/animations
 */
public abstract class PSPOIAnnotation extends PSAnimatablePanel implements PSCalloutHandler{

	protected View pin;
	protected View pinstand;
	protected double latitude;
	protected double longitude;
	protected GeoPoint point;
	public PSWayPoint waypoint;
	protected Boolean isSelected;
	protected PSAnnotationsManager manager;
	protected PSPOICallout callout;
	protected PSAnnotationHandler delegate;
	private Boolean hasCallout;
	private Animation animation;
	public Boolean hasCallout(){
		return hasCallout;
	}
	public PSPOIAnnotation(Context ctx, PSWayPoint poi){
		super(ctx);
		waypoint = poi;
		latitude = waypoint.latitude;
		longitude = waypoint.longitude;
		point = new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
		isOpen = false;
		isSelected = false;
		hasCallout = false;
	}
	public final LayoutParams getAnnotationLayoutParams(){
		return new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6)), MapView.LayoutParams.BOTTOM_CENTER);
	}
	public final LayoutParams getCalloutLayoutParams(){
		return new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6)), MapView.LayoutParams.BOTTOM_CENTER);
	}
	public void setCallout(PSPOICallout cout){
		if(cout != null){
			hasCallout = true;
			callout = cout;	
			callout.delegate = this;
		}
	}
	public final View getView(){
		
		container = getContainerView();
		container.setId(1);
		pinstand =getPinStandView();
		pinstand.setId(2);
		container.addView(pinstand);
        pin = getPinView();
        pin.setId(3);
        container.addView(pin);
        pin.setOnClickListener(new OnClickListener(){
        	public void onClick(View vw){
        		isSelected = !isSelected;
        		delegate.selectedAnnotation(PSPOIAnnotation.this);
    		}
        });
        
        return container;
	}
	
	
	public abstract ViewGroup getContainerView();
	public abstract View getPinView();
	public abstract View getPinStandView();
	public abstract Animation getOpenAnimation();
	public abstract Animation getCloseAnimation();
	
	public final void animateOpen(){
		annotationOpenAnimation();
	}
	private void annotationOpenAnimation(){
		if(!isOpen){
		container.setDrawingCacheEnabled(true);
		animation = getOpenAnimation();
		if(animation != null){
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				delegate.annnotationOpenAnimationEnd(PSPOIAnnotation.this);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				delegate.annnotationOpenAnimationStart(PSPOIAnnotation.this);
			}
        	
        });
		pin.startAnimation(animation);
		}else{
			delegate.annnotationOpenAnimationStart(PSPOIAnnotation.this);
			delegate.annnotationOpenAnimationEnd(PSPOIAnnotation.this);
		}
		isOpen = true;
		}
	}
	public final void animateClose(){
		annotationCloseAnimation();
		animateCalloutClose();
	}
	public final void animateCalloutOpen(){
		if(callout != null)
			callout.animateOpen();
	}
	public final void animateCalloutClose(){
		if(callout != null)		
			callout.animateClose();
}
	private void annotationCloseAnimation(){
		if(isOpen){
		animation = getCloseAnimation();
		if(animation != null){
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				delegate.annnotationCloseAnimationEnd(PSPOIAnnotation.this);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				delegate.annnotationCloseAnimationStart(PSPOIAnnotation.this);
			}
        	
        });
		pin.startAnimation(animation);
		}else{
			delegate.annnotationCloseAnimationStart(PSPOIAnnotation.this);
			delegate.annnotationCloseAnimationEnd(PSPOIAnnotation.this);
		}
		isOpen = false;
		}
	}
	@Override
	public void calloutBodyTapped(View view) {
		// TODO Auto-generated method stub
		delegate.calloutBodyTapped(view, this);
	}
	@Override
	public void calloutAccessoryTapped(View view) {
		// TODO Auto-generated method stub
		delegate.calloutAccessoryTapped(view, this);
	}
	@Override
	public void calloutOpenAnimationStart() {
		// TODO Auto-generated method stub
		delegate.calloutOpenAnimationStart(this);
	}
	@Override
	public void calloutOpenAnimationEnd() {
		// TODO Auto-generated method stub
		delegate.calloutOpenAnimationEnd(this);
	}
	@Override
	public void calloutCloseAnimationStart() {
		delegate.calloutCloseAnimationStart(this);
	}
	@Override
	public void calloutCloseAnimationEnd() {
		delegate.calloutCloseAnimationEnd(this);
	}
	protected interface PSAnnotationHandler{
		public void selectedAnnotation(PSPOIAnnotation annotation);
		public void deselectedAnnotation(PSPOIAnnotation annotation);
		public void calloutAccessoryTapped(View v, PSPOIAnnotation annotation);
		public void calloutBodyTapped(View v, PSPOIAnnotation annotation);
		
		public void annnotationOpenAnimationStart(PSPOIAnnotation annotation);
		public void annnotationOpenAnimationEnd(PSPOIAnnotation annotation);
		public void annnotationCloseAnimationStart(PSPOIAnnotation annotation);
		public void annnotationCloseAnimationEnd(PSPOIAnnotation annotation);
		
		public void calloutOpenAnimationStart(PSPOIAnnotation annotation);
		public void calloutOpenAnimationEnd(PSPOIAnnotation annotation);
		public void calloutCloseAnimationStart(PSPOIAnnotation annotation);
		public void calloutCloseAnimationEnd(PSPOIAnnotation annotation);
	}

	
}
