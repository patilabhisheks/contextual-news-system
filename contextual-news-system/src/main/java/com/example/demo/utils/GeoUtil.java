package com.example.demo.utils;

public final class GeoUtil {
    private static final double R = 6371.0088; // km

    public static double distanceKm(double lat1,double lon1,double lat2,double lon2){
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.pow(Math.sin(dLat/2),2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon/2),2);
        return 2*R*Math.asin(Math.sqrt(a));
    }
}
