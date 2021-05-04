package sg.edu.smu.xposedmoduledemo.hooks;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.util.Util;

public class LocationSingleUpdate implements HookTemplate{
    private static final String className = "android.location.LocationManager";
    private Context context;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {

        Log.d("Mulin", "before location single update");
        if (methodHookParam.args.length == 2){
            Log.d("Mulin", "arg length is 2");
        }
        if (methodHookParam.args.length == 3){
            Log.d("Mulin", "arg length is 3");
        }
        if (i == 3) {
            if (methodHookParam.args.length >= 3 && (methodHookParam.args[1] instanceof LocationListener)) {

                LocationListener ll = (LocationListener) methodHookParam.args[1];

                Class<?> clazz = LocationListener.class;
                Method m = null;
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals("onLocationChanged") && !Modifier.isAbstract(method.getModifiers())) {
                        m = method;
                        break;
                    }
                }

                try {
                    if (m != null) {
                        Location l = new Location(LocationManager.GPS_PROVIDER);
                        l.setLatitude(1.3535d);
                        l.setLongitude(103.9879d);
                        l.setAccuracy(100f);
                        l.setTime(System.currentTimeMillis());
                        l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        m.invoke(ll, l);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Member getCallable(Class<?> cls) {

        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (m.getName().equals("requestSingleUpdate") && m.getParameterCount() == 3){
                    Log.d("Mulin", "hook request single updated");
                    return m;
                }
            }
        }
        Log.d("Mulin", "getCallable: single update");
        return null;

    }

    @Override
    public String getClassName() {
        return "android.location.LocationManager";
    }

    @Override
    public int getOp() {
        return 1;
    }

    @Override
    public boolean shouldHook(Object obj, Object... objArr) {
        return true;
    }

    @Override
    public String toString(){ return "LocationSingleUpdate";}
}
