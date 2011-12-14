package com.precisosol.annotationstest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.precisosol.annotations.PSAnnotationsManager;
import com.precisosol.annotations.PSPOIAnnotation;
import com.precisosol.annotations.PSPOICallout;
import com.precisosol.annotations.PSWayPoint;
import com.precisosol.annotations.R;
import com.precisosol.annotations.PSAnnotationsManager.PSAnnotationsHandler;
import com.precisosol.annotations.R.id;
import com.precisosol.annotations.R.layout;

public class AnnotationsActivity extends MapActivity implements PSAnnotationsHandler{
	MapView mapView;
	PSAnnotationsManager annotationsManager;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.mapview);
        annotationsManager = new PSAnnotationsManager(this,mapView,true);
        annotationsManager.delegate = this;
        PSConcretePOIAnnotation annotation = new PSConcretePOIAnnotation(this, new PSWayPoint("Title","Subtitle",33.584145,-85.061439));
        annotation.setCallout(new PSConcretePOICallout(this,annotation));
        annotationsManager.addAnnotation(annotation);
        
    }
    
    /** MapActivity Method */
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
	public void selectedAnnotation(MapView map, PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deselectedAnnotation(MapView map, PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calloutAccessoryTapped(MapView map, View v,
			PSPOIAnnotation annotation) {
		
		
	}

	@Override
	public void calloutBodyTapped(MapView map, View v,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void annnotationOpenAnimationStart(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void annnotationOpenAnimationEnd(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void annnotationCloseAnimationStart(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void annnotationCloseAnimationEnd(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calloutOpenAnimationStart(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calloutOpenAnimationEnd(MapView map, PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calloutCloseAnimationStart(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calloutCloseAnimationEnd(MapView map,
			PSPOIAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

}