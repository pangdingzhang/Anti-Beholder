package sg.edu.smu.xposedmoduledemo.hooks;

import android.location.Location;

import sg.edu.smu.xposedmoduledemo.util.Util;

public class FakeLocation extends Location {
    public FakeLocation() {
        super("gps");
    }

    public FakeLocation(Location l) {
        super(l);
    }

    public double getLongitude() {
        return Util.getFakeLng();
    }

    public double getLatitude() {
        return Util.getFakeLat();
    }

}
