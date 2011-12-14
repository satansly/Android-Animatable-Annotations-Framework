package com.precisosol.annotations;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public abstract class PSPOICallout extends PSAnimatablePanel{
	protected View background;
	protected View btnAccessory;
	protected View title;
	protected View subtitle;
	protected Boolean isOpen;
	protected PSWayPoint waypoint;
	protected PSCalloutHandler delegate;
	protected PSPOIAnnotation containerAnnotation;
	public PSPOICallout(Context ctx, PSPOIAnnotation ann){
		super(ctx);
		waypoint = ann.waypoint;
		isOpen = false;
		containerAnnotation = ann;
	}
	public final View getView(){
		container = getContainerView();
		container.setId(1);
		background =  getBackgroundView();
		background.setId(2);
		container.addView(background);
		background.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				return true;
			}
        	
        });
		
		title = getTitleView();
		title.setId(3);
		container.addView(title);
		if(title.getClass() == TextView.class){
			((TextView) title).setText(waypoint.title);
		}
        subtitle = (TextView) getSubtitleView();
        subtitle.setId(4);
        container.addView(subtitle);
        if(subtitle.getClass() == TextView.class){
        	((TextView)subtitle).setText(waypoint.subtitle);
        }
        btnAccessory = getAccessoryView();
        btnAccessory.setId(5);
        container.addView(btnAccessory);
        btnAccessory.setOnClickListener(new OnClickListener(){
        	public void onClick(View vw){
        		if(delegate != null)
        			delegate.calloutAccessoryTapped(container);
        		
    		}
        });
        container.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				return false;
			}
        	
        });
        /*
        container.post(new Runnable(){
        	public void run(){
        		container.setVisibility(View.INVISIBLE);
        	}
        });*/
        return container;
	}
	public abstract ViewGroup getContainerView();
	public abstract View getBackgroundView();
	public abstract View getTitleView();
	
	public abstract View getSubtitleView();
	public abstract View getAccessoryView();
	
	public abstract Animation getOpenAnimation();
	public abstract Animation getCloseAnimation();
	
	public final void animateOpen(){
		container.setDrawingCacheEnabled(true);
		container.setVisibility(View.VISIBLE);
		animation = getOpenAnimation();
		if(animation != null){
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				delegate.calloutOpenAnimationEnd();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				delegate.calloutOpenAnimationStart();
			}
        	
        });
		container.startAnimation(animation);
		}else{
			delegate.calloutOpenAnimationStart();
			delegate.calloutOpenAnimationEnd();
		}
		isOpen = true;
	}
	public final void animateClose(){
		 	animation = getCloseAnimation();
		 	if(animation !=  null){
			animation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationEnd(Animation animation) {
					delegate.calloutCloseAnimationEnd();
					if(container.getVisibility() == View.VISIBLE){
						container.setVisibility(View.INVISIBLE);
						
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					
					
				}

				@Override
				public void onAnimationStart(Animation animation) {
					delegate.calloutCloseAnimationStart();
				}
            	
            });
            container.startAnimation(animation);
		 	}else{
		 		delegate.calloutCloseAnimationStart();
		 		delegate.calloutCloseAnimationEnd();
		 		container.setVisibility(View.INVISIBLE);
		 			
		 	}
		isOpen = false;
	}
	protected interface PSCalloutHandler {
		public void calloutBodyTapped(View view);
		public void calloutAccessoryTapped(View view);
		public void calloutOpenAnimationStart();
		public void calloutOpenAnimationEnd();
		public void calloutCloseAnimationStart();
		public void calloutCloseAnimationEnd();
		
	}
}
