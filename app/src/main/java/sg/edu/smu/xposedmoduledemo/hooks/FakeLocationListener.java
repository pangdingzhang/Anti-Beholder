package sg.edu.smu.xposedmoduledemo.hooks;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import sg.edu.smu.xposedmoduledemo.util.Util;

public class FakeLocationListener implements LocationListener {
    LocationListener l;

    public FakeLocationListener(LocationListener l) {
        this.l = l;
    }

    @Override
    public void onLocationChanged(Location location) {
        Util.modifyLocation(location);
        this.l.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        this.l.onStatusChanged(s, i, bundle);
    }

    @Override
    public void onProviderEnabled(String s) {
        this.l.onProviderEnabled(s);

    }

    @Override
    public void onProviderDisabled(String s) {
        this.l.onProviderDisabled(s);
    }
}
