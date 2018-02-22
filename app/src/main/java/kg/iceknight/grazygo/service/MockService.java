package kg.iceknight.grazygo.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public class MockService extends Service {

    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager = null;
    }

    @Override
    @SuppressLint("MissingPermission")
    public int onStartCommand(Intent intent, int flags, int startId) {
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                setLocation(GeoService.calcNextCoord(currentLocation, 50L * i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            disableMockLocation();
            stopSelf(startId);
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("NewApi")
    private Location setLocation(Location mockLocation) {

        Location location = new Location(LocationManager.GPS_PROVIDER);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
                false, false, true,
                true, true, 0, 1);
        location.setLatitude(mockLocation.getLatitude());
        location.setLongitude(mockLocation.getLongitude());
        location.setAccuracy(10);
        location.setTime(System.currentTimeMillis());
        location.setSpeed(150F);
        location.setBearing(45F);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER,LocationProvider.AVAILABLE,null,System.currentTimeMillis());
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
        return mockLocation;

    }

    @SuppressLint("MissingPermission")
    public void disableMockLocation() {
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        setLocation(currentLocation);
        if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }
    }
}
