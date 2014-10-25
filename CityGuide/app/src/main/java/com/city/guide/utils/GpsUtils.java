package com.city.guide.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public class GpsUtils {
    private static GpsUtils INSTANCE;
    private static final long MIN_TIME = 3000;
    private static final float MIN_DISTANCE = 10;

    private static double sLat;
    private static double sLng;

    public static GpsUtils getInstance(Context context, CurrentLocationListener listener) {
        if (INSTANCE == null) {
            getLocation(context, listener);
            INSTANCE = new GpsUtils();
        }
        return INSTANCE;
    }

    private static void getLocation(Context context, final CurrentLocationListener listener) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        sLat = location.getLatitude();
                        sLng = location.getLongitude();
                        if (listener != null) {
                            listener.onLocationChanged();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    public double getLatitude() {
        return sLat;
    }

    public double getLongitude() {
        return sLng;
    }

    public interface CurrentLocationListener {
        public void onLocationChanged();
    }
}
