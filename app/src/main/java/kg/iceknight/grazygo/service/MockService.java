package kg.iceknight.grazygo.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;
import static kg.iceknight.grazygo.common.Constants.LOG_TAG;

public class MockService {

    private Context context;
    private LocationManager locationManager;
    private Location currentLocation;
    private boolean isMockActivated = false;

    @SuppressLint("MissingPermission")
    public MockService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public void walk() {
        if (!isMockActivated) {
            isMockActivated = true;
            if (null != ServiceCollection.getChoosedLocation()) {
                currentLocation = ServiceCollection.getChoosedLocation();
            }
        }
        currentLocation = GeoService.calcNextCoord(currentLocation, 50);
        setLocation(currentLocation);
    }

    public void jump(Integer distance) {
        if (null == ServiceCollection.getChoosedLocation()) {
            setLocation(GeoService.calcNextCoord(currentLocation, distance));
        } else {
            setLocation(GeoService.calcNextCoord(ServiceCollection.getChoosedLocation(), distance));
        }

    }

    @SuppressLint("NewApi")
    public Location setLocation(@NonNull Location mockLocation) {
        Log.d(LOG_TAG, "MockService setLocation " + mockLocation.toString());
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

