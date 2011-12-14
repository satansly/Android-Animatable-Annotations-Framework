package com.precisosol.annotations;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

public abstract class PSAnimatablePanel {
	protected ViewGroup container;
	protected Context context;
	protected Boolean isOpen;
	protected Animation animation;
	protected PSAnimationHandler animationDelegate;
	public PSAnimatablePanel(Context ctx){
		context = ctx;
	}
	public abstract View getView();
	public abstract void animateOpen();
	public abstract void animateClose();
	
	public interface PSAnimationHandler{
		public void onOpenAnimationStart();
		public void onOpenAnimationEnd();
		public void onCloseAnimationStart();
		public void onCloseAnimationEnd();
	}
}
