package kg.iceknight.grazygo.service;

import android.location.Location;
import android.location.LocationManager;

public class GeoService {
    public static Location calcNextCoord(Location location, Integer offset) {
        if (location == null) {
            return null;
        }
        Double latitude = location.getLatitude();
        Double longitute = location.getLongitude();

        Double delta = offset / 13.7D;

        latitude += (delta/10000D);
        longitute += (delta/10000D);

        Location convertedLocation = new Location(LocationManager.GPS_PROVIDER);

        convertedLocation.setLatitude(latitude);
        convertedLocation.setLongitude(longitute);

        return convertedLocation;
    }
}
