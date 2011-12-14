package com.precisosol.annotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.precisosol.annotations.PSPOIAnnotation.PSAnnotationHandler;


/**
 * Manages annotation
 * Keeps record of annotations added to map view
 * keeps record of selected annotation
 * Delegates event to annotation to when touched
 * Manages showing/hiding user location
 */
public class PSAnnotationsManager implements PSAnnotationHandler{

	public ArrayList<PSPOIAnnotation> annotations; 
	PSPOIAnnotation selectedAnnotation;
	Boolean showsUserLocation;
	MapView mapView;
	Context context;
	public PSAnnotationsHandler delegate;
	int minLatitude,minLongitude = Integer.MIN_VALUE;
	int maxLatitude,maxLongitude = Integer.MAX_VALUE;
	boolean showingUserLocation = false;
	GeoPoint p = null;
	private LocationManager lm;
	private LocationListener ll;
	GeoPoint previousFocusPoint;
	GestureDetector gestureDetector;
	public PSAnnotationsManager(Context ctx, MapView mapview, boolean showUserLocation){
		mapView = mapview;
		context = ctx;
		annotations = new ArrayList<PSPOIAnnotation>();
		showsUserLocation = showUserLocation;
		setShowsUserLocation(showsUserLocation);
		gestureDetector = new GestureDetector(new PSGestureDetector());
		mapView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(gestureDetector.onTouchEvent(arg1) == true){
				//If touch was not on callout or annotation
				int x = (int)arg1.getRawX();
				int y = (int)arg1.getRawY();
				Rect calloutRect = new Rect();
				Rect pinRect = new Rect();
				
				if(selectedAnnotation != null){
					selectedAnnotation.pin.getGlobalVisibleRect(pinRect);
					
					if(pinRect.contains(x, y) == true){
						return true;
					}else if(selectedAnnotation.callout.isOpen){
						selectedAnnotation.callout.container.getGlobalVisibleRect(calloutRect);
						if(calloutRect.contains(x, y) == true){
							return true;
						}else{
							selectedAnnotation(selectedAnnotation);
							return true;
						}
					}else{
						selectedAnnotation(selectedAnnotation);
						return true;
					}
				}
				return false;
			}
				return false;
			}
        	
        });
		
	}
	public void toggleFocus(){
		if(!showingUserLocation){
			if(p != null){
				MapController controller = mapView.getController();
				controller.setZoom(17); 
	    		controller.animateTo(p);
			}
		}else{
			focusAllAnnotations();
				
		}
		showingUserLocation = !showingUserLocation;
	}
	public void setShowsUserLocation(boolean show){
		if(show){
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
	    List<Overlay> list = mapView.getOverlays();
	    list.add(myLocationOverlay);

	    lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

	    ll = new MyLocationListener();
	    Criteria c = new Criteria();
	    
	    String lp = lm.getBestProvider(c, true);
	    if(lp != null){
	    lm.requestLocationUpdates(
	            lp,
	            0,
	            0,
	            ll);
	    Location l = lm.getLastKnownLocation(lp);
	    if(l != null){
	    //Get the current location in start-up
	    	p = new GeoPoint(
	           (int)(l.getLatitude()*1000000),
	           (int)(l.getLongitude()*1000000));
	    	MapController controller = mapView.getController();
	    	controller.animateTo(p);	
	    }
	    }
		}else{
			List<Overlay> list = mapView.getOverlays();
			list.clear();
		}


	}
	public void addAnnotation(PSPOIAnnotation annotation){
		//Find appropriate place for the annotation and insert
		int x = 0;
		mapView.addView(annotation.getView(),0,annotation.getAnnotationLayoutParams());
		annotations.add(x,annotation);
		Collections.sort(annotations, new PSPOIAnnotationComparator());
		annotation.delegate = this;
		annotation.manager = this;
		
	}
	public void addAnnotations(ArrayList<PSPOIAnnotation> _annotations){
		for(PSPOIAnnotation annotation: _annotations){
        	addAnnotation(annotation);
        }
	}
	
	public void removeAnnotation(PSPOIAnnotation annotation){
		mapView.removeView(annotation.container);
		annotations.remove(annotation);
		annotation.manager = null;
		annotation.delegate = this;
		mapView.removeView(annotation.callout.container);
	}
	public void removeAllAnnotations(){
		
		while(annotations.size() > 0){
        	removeAnnotation(annotations.get(annotations.size() -1));
        }
		
	}
	public void focusAnnotation(PSPOIAnnotation annotation){
		MapController controller = mapView.getController();
		GeoPoint p = new GeoPoint((int) (annotation.latitude * 1E6), 
			    (int) (annotation.longitude * 1E6));
		previousFocusPoint = p;
		controller.animateTo(p);
		controller.setZoom(17); 
	}
	public void focusAllAnnotations(){
		setMinMaxValues();
		MapController controller = mapView.getController();
		controller.zoomToSpan((maxLatitude - minLatitude), (maxLongitude - minLongitude));
		GeoPoint p = new GeoPoint(
				(int)(maxLatitude + minLatitude)/2,

                (int)(maxLongitude + minLongitude)/2 );
		previousFocusPoint = p;
		controller.animateTo(p);
	}
	public void selectAnnotation(PSPOIAnnotation annotation){
		this.selectedAnnotation(annotation);
			
		
	}
	
	public void setMinMaxValues(){
		if(annotations.size() == 1){
			minLatitude = annotations.get(0).point.getLatitudeE6();
			minLongitude = annotations.get(0).point.getLongitudeE6();
		    maxLatitude  = annotations.get(0).point.getLatitudeE6();
		    maxLongitude = annotations.get(0).point.getLongitudeE6();
		}else{
			for(PSPOIAnnotation annotation: annotations){
				minLatitude  = Math.min( annotation.point.getLatitudeE6(), minLatitude );
		    	minLongitude = Math.min( annotation.point.getLongitudeE6(), minLongitude);
		    	maxLatitude  = Math.max( annotation.point.getLatitudeE6(), maxLatitude );
		    	maxLongitude = Math.max( annotation.point.getLongitudeE6(), maxLongitude );
		    	mapView.bringChildToFront(annotation.container);
        	}
		}
	}
	@Override
	public void selectedAnnotation(PSPOIAnnotation annotation) {
		if(selectedAnnotation != null){
			if(selectedAnnotation != annotation){
				selectedAnnotation.animateClose();
				deselectedAnnotation(selectedAnnotation);
				selectedAnnotation = annotation;
				delegate.selectedAnnotation(mapView, annotation);
				selectedAnnotation.animateOpen();
				focusAnnotation(annotation);
			}
			
		else if(selectedAnnotation == annotation){
			selectedAnnotation.animateClose();
			deselectedAnnotation(selectedAnnotation);
			selectedAnnotation  = null;
			//focusAllAnnotations();
		}
			
		}else{
			selectedAnnotation = annotation;
			delegate.selectedAnnotation(mapView, annotation);
			selectedAnnotation.animateOpen();
			focusAnnotation(annotation);
		}
	}
	@Override
	public void deselectedAnnotation(PSPOIAnnotation annotation) {
		
		delegate.deselectedAnnotation(mapView, annotation);
	}
	@Override
	public void calloutAccessoryTapped(View v,
			PSPOIAnnotation annotation) {
		delegate.calloutAccessoryTapped(mapView, v, annotation);
	}
	@Override
	public void annnotationOpenAnimationStart(PSPOIAnnotation annotation) {
		delegate.annnotationOpenAnimationStart(mapView, annotation);
	}
	@Override
	public void annnotationOpenAnimationEnd(PSPOIAnnotation annotation) {
		if(annotation.hasCallout()){
			mapView.addView(annotation.callout.getView(),annotation.getCalloutLayoutParams());
			annotation.callout.container.setVisibility(View.INVISIBLE);
			mapView.bringChildToFront(annotation.callout.container);
			annotation.animateCalloutOpen();
		}
		delegate.annnotationOpenAnimationEnd(mapView, annotation);
			
	}
	@Override
	public void annnotationCloseAnimationStart(PSPOIAnnotation annotation) {
		delegate.annnotationCloseAnimationStart(mapView, annotation);
	}
	@Override
	public void annnotationCloseAnimationEnd(PSPOIAnnotation annotation) {
		delegate.annnotationCloseAnimationEnd(mapView, annotation);
	}
	@Override
	public void calloutOpenAnimationStart(PSPOIAnnotation annotation) {
		delegate.calloutOpenAnimationStart(mapView, annotation);
	}
	@Override
	public void calloutOpenAnimationEnd(PSPOIAnnotation annotation) {
		delegate.calloutOpenAnimationEnd(mapView, annotation);
	}
	@Override
	public void calloutCloseAnimationStart(
			PSPOIAnnotation annotation) {
		delegate.calloutCloseAnimationStart(mapView, annotation);
	}
	@Override
	public void calloutCloseAnimationEnd(
			PSPOIAnnotation annotation) {
		delegate.calloutCloseAnimationEnd(mapView, annotation);
		if(annotation.hasCallout()){
			annotation.callout.container.clearAnimation();
			mapView.removeView(annotation.callout.container);
		}
	}
	
	public interface PSAnnotationsHandler{
		
		
		public void selectedAnnotation(MapView map,PSPOIAnnotation annotation);
		public void deselectedAnnotation(MapView map,PSPOIAnnotation annotation);
		public void calloutAccessoryTapped(MapView map, View v, PSPOIAnnotation annotation);
		public void calloutBodyTapped(MapView map, View v, PSPOIAnnotation annotation);
		
		public void annnotationOpenAnimationStart(MapView map,PSPOIAnnotation annotation);
		public void annnotationOpenAnimationEnd(MapView map,PSPOIAnnotation annotation);
		public void annnotationCloseAnimationStart(MapView map,PSPOIAnnotation annotation);
		public void annnotationCloseAnimationEnd(MapView map,PSPOIAnnotation annotation);
		
		public void calloutOpenAnimationStart(MapView map,PSPOIAnnotation annotation);
		public void calloutOpenAnimationEnd(MapView map,PSPOIAnnotation annotation);
		public void calloutCloseAnimationStart(MapView map,PSPOIAnnotation annotation);
		public void calloutCloseAnimationEnd(MapView map,PSPOIAnnotation annotation);
	}

	@Override
	public void calloutBodyTapped(View v, PSPOIAnnotation annotation) {
		delegate.calloutBodyTapped(mapView, v, annotation);
	}
	
	protected class MyLocationOverlay extends com.google.android.maps.Overlay {

		
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            Paint paint = new Paint();

            super.draw(canvas, mapView, shadow);
            // Converts lat/lng-Point to OUR coordinates on the screen.
            Point myScreenCoords = new Point();
            if(p != null){
            mapView.getProjection().toPixels(p, myScreenCoords);

            paint.setStrokeWidth(1);
            paint.setARGB(255, 255, 255, 255);
            paint.setStyle(Paint.Style.STROKE);
            
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.blip);
            bmp = getResizedBitmap(bmp,16,16);
            canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
            Bitmap radar = BitmapFactory.decodeResource(context.getResources(), R.drawable.radious);
            radar = getResizedBitmap(radar,100,100);
            canvas.drawBitmap(radar, myScreenCoords.x - 42, myScreenCoords.y - 42, paint);
            
            }
            return true;
        }
    }
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		
		return resizedBitmap;
		}

	private class MyLocationListener implements LocationListener{

	      public void onLocationChanged(Location argLocation) {
	       // TODO Auto-generated method stub
	       p = new GeoPoint(
	        (int)(argLocation.getLatitude()*1000000),
	        (int)(argLocation.getLongitude()*1000000));
	      }

	      public void onProviderDisabled(String provider) {
	       // TODO Auto-generated method stub
	      }

	      public void onProviderEnabled(String provider) {
	       // TODO Auto-generated method stub
	      }

	      public void onStatusChanged(String provider,
	        int status, Bundle extras) {
	       // TODO Auto-generated method stub
	      }
	     }    
	protected boolean isRouteDisplayed() {
	    return false;
	}
	public class PSPOIAnnotationComparator implements Comparator<PSPOIAnnotation> {

		@Override
		public int compare(PSPOIAnnotation lhs, PSPOIAnnotation rhs) {
			// TODO Auto-generated method stub
			if(lhs.latitude > rhs.latitude)
				return -1;
			else if(lhs.latitude == rhs.latitude)
				return 0;
			else
				return 1;
			
		}

	}
	class PSGestureDetector extends SimpleOnGestureListener {
        
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return true;
        }

    }

}
