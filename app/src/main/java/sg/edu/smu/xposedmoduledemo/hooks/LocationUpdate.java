package sg.edu.smu.xposedmoduledemo.hooks;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.util.Util;

public class LocationUpdate implements HookTemplate{
    private Context context;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {

        Log.d("Mulin", "before location update");
        if (methodHookParam.args.length == 4 ){
            Log.d("Mulin", "why blank?");
        }
        if (i == 3) {
            Log.d("Mulin", "i=3");
//            if (methodHookParam.args[3] != null) {
//            PendingIntent intent = (PendingIntent) methodHookParam.args[3];
//            Intent fakeRes = new Intent();
//            android.location.Location l = new android.location.Location("passive");
//            Util.modifyLocation(l);
//            fakeRes.putExtra("location", l);
//            Log.d("Mulin", "fake location");
//            try {
//                intent.send(this.context, 0, fakeRes);
//            } catch (PendingIntent.CanceledException e) {
//            }
//            methodHookParam.args[3] = null;
//            }
//            if (methodHookParam.args[1] != null) {
            Log.d("Mulin", "Fake Location listener");
            methodHookParam.args[1] = new FakeLocationListener((LocationListener) methodHookParam.args[1]);
//            }
        }

    }

    @Override
    public Member getCallable(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (m.getName().equals("requestLocationUpdates")) {
                    m.setAccessible(true);
                    return m;
                }
            }
        }
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
    public String toString(){ return "LocationUpdate";}
}
