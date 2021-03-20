package me.pete.ocarinalibrary.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import androidx.annotation.NonNull;
import me.pete.ocarinalibrary.listener.OnGetCoordinateListener;

/**
 * Created by Priyanto Tantowi.
 *
 * CoordinateHelper is helper for all about coordinate handler.
 * This function using GPS chipset and Google Play Service.
 */
public final class CoordinateHelper {
    private boolean check;
    private FusedLocationProviderClient fusedLocationClient;
    private int time;
    private static Location location;

    @SuppressLint("LongLogTag")
    private void getCoordinateUsingGPSOnly(final Context context, final OnGetCoordinateListener onGetCoordinateListener) {
        check = true;
        time = 0;
        String contextLocation = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) context.getSystemService(contextLocation);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGetCoordinateListener.onLocation(0.0f, 0.0f);
            Log.i("CoordinateHelper-getCoordinate", "GPS OFF");
        } else {
            try {
                String provider = locationManager.getBestProvider(CoordinateHelper.onGetCriteria(), true);
                location = locationManager.getLastKnownLocation(provider);
                Log.i("CoordinateHelper", "getCoordinateUsingGPSOnly");
                if (location != null) {
                    check = false;
                    onGetCoordinateListener.onLocation(location.getLatitude(), location.getLongitude());
                    Log.i("CoordinateHelper-getCoordinate", "Location: " + location.getLatitude() + " " + location.getLongitude());
                } else {
                    onGetCoordinateListener.onLocation(0, 0);
                    //locationManager.requestLocationUpdates(provider, 2000, 1, locationListener);
                }
            } catch (SecurityException e) {
                onGetCoordinateListener.onLocation(0.0f, 0.0f);
                Log.i("CoordinateHelper-getCoordinate", "GPS SecurityException");
            } catch (Exception e) {
                onGetCoordinateListener.onLocation(0.0f, 0.0f);
                Log.i("CoordinateHelper-getCoordinate", "GPS Exception " + e.toString());
            }
        }
    }

    private void getCoordinateGooglePlayService(final Context context, final OnGetCoordinateListener onGetCoordinateListener) {
        check = true;
        time = 0;
        LocationCallback locationCallback = new LocationCallback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    location = locationList.get(locationList.size() - 1);
                    check = false;
                    Log.i("CoordinateHelper", "getCoordinateGooglePlayService");
                    Log.i("CoordinateHelper-GMS", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    onGetCoordinateListener.onLocation(location.getLatitude(), location.getLongitude());
                } else {
                    Log.i("CoordinateHelper", "Masuk else");
                    getCoordinateUsingGPSOnly(context, onGetCoordinateListener);
                }
                fusedLocationClient.removeLocationUpdates(this);
            }
        };

        if(isGooglePlayServicesAvailable(context)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                onGetCoordinateListener.onLocation(location.getLatitude(), location.getLongitude());
                            } else {
                                getCoordinateUsingGPSOnly(context, onGetCoordinateListener);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("CoordinateHelper-Last", e.toString());
                            getCoordinateUsingGPSOnly(context, onGetCoordinateListener);
                        }
                    });
        } else {
            Log.i("CoordinateHelper-Last", "Google play service not available");
        }
    }

    /**
     * This function is handling to get coordinate.
     * System will find coordinate using Google Play Service.
     * If Google Play Service doesn't feedback, not response or last
     * location is not found then system try find coordinate using
     * GPS network. If GPS network still not found a location then
     * function will returns latitude 0.0 and longitude 0.0
     */
    @SuppressLint("LongLogTag")
    public void getCoordinate(Context context, final OnGetCoordinateListener onGetCoordinateListener) {
        if(PhoneHelper.isMobileDataActive(context)) {
            getCoordinateGooglePlayService(context, onGetCoordinateListener);
        } else {
            getCoordinateUsingGPSOnly(context, onGetCoordinateListener);
        }
    }

    private static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLatitude, float theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }

    private static Criteria onGetCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setSpeedRequired(true);
        return criteria;
    }

    /**
     * This function returns distance between 2 coordinate(latitude, longitude) in meters.
     *
     * @param lat1  First latitude
     * @param lon1  First longitude
     * @param lat2  Second latitude
     * @param lon2  Second longitude
     * @param el1   First height, you can put 0 for this parameter
     * @param el2   Second height, you can put 0 for this parameter
     * @return
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * This function used for update location every 1 seconds using
     * Google Play Service.
     */
    @SuppressLint({"LongLogTag", "MissingPermission"})
    public static void onRequestUpdate(Context context) {
        LocationCallback locationCallback = new LocationCallback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(CoordinateHelper.onGetCriteria(), true);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.i("CoordinateHelper-UpdateGPS", "Location: " + location.getLatitude() + " " + location.getLongitude());
            }

            public void onProviderDisabled(String provider) {
                Log.i("CoordinateHelper-Update", "GPS not active");
            }

            public void onProviderEnabled(String provider) {
                Log.i("CoordinateHelper-Update", "GPS is active");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
    }

    /**
     * This function returns boolean value. The boolean is true if
     * Google Play Service is available on your phone and false if
     * Google Play Service is unavailable.
     */
    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if(status != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * This function returns postal code using coordinate(latitude and longitude)
     */
    public static String getPostalCode(Context context, double latitude, double longitude) {
        String result = "";
        final Geocoder gcd = new Geocoder(context);
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address address : addresses) {
                if(address.getPostalCode()!=null && !address.getPostalCode().contentEquals("")) {
                    result = address.getPostalCode();

                }
            }
        } catch (Exception e) {

        }
        return result;
    }
}
