package com.precisosol.annotations;




public class PSWayPoint {
	static int totalitemscount = 0;
	    public int uniqueidentifier = 0;
	    public String  title;
	    public String  subtitle;
	    public double latitude;
	    public double longitude;
	    
	    public PSWayPoint(String tit, String subtit, double lat, double lon){
	    	uniqueidentifier = PSWayPoint.totalitemscount += 1;
	    	latitude = lat;
	    	longitude = lon;
	    	title = tit;
	    	subtitle = subtit;
	    	
	    }

}
