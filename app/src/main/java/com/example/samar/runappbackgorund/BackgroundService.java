package com.example.samar.runappbackgorund;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BackgroundService extends Service implements LocationListener {
    private NotificationManager mNM;
    Notification notification;
    Bundle b;
    Intent notificationIntent;
    private final IBinder mBinder = new LocalBinder();
    private String newtext;

    protected LocationManager locationManager;

    final int REFRESHTIME = 100;
    final float MINDISTANCE = 0;

    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        newtext = "BackGroundApp Service Running";

        Log.d("##NOTIFY", "Running");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESHTIME, MINDISTANCE, this);



        Intent intent = new Intent("com.example.samar.runappbackgorund.BackgroundAppExample");

        PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(BackgroundService.this);

        builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("WhatsApp Notification");
        builder.setContentText("You have a new message");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

        notification = builder.getNotification();
        mNM.notify(11, notification);

        showNotification();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    public void onDestroy() {
       /* mNM.cancel(R.string.local_service_started);
        stopSelf();*/
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification() {
        CharSequence text = getText(R.string.local_service_started);


        Intent intent = new Intent("com.example.samar.runappbackgorund.BackgroundAppExample");

        PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(BackgroundService.this);

        builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle(text);
        builder.setContentText(newtext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

        notification = builder.getNotification();
        mNM.notify(11, notification);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onLocationChanged(Location location) {

        getAddress(location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void getAddress(Location location) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());



        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        String fullAddress = address + ", " + city + ", " + state + ", " + country + ", " + postalCode + ", " + knownName;
      //  tvAddress.setText(fullAddress);

        Log.d("#", "=======================ADDRESS========================");
        Log.d("#Latitude-value", location.getLatitude() + "");
        Log.d("#Longitude-value", location.getLongitude() + "");
        Log.d("#Address", address);
        Log.d("#City", city);
        Log.d("#State", state);
        Log.d("#Country", country);
        Log.d("#Postal Code", postalCode);
        Log.d("#knownName", knownName);

    }
}