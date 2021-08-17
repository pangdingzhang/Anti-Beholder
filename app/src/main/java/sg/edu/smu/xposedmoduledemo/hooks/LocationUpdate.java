package sg.edu.smu.xposedmoduledemo.hooks;

import android.app.AndroidAppHelper;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.actions.SearchIntents;

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
        if (methodHookParam.args.length == 4){
            Log.d("Mulin", "arg 0 is "+ methodHookParam.args[0].toString());


            Log.d("Mulin", "Is arg 3 null?"+ (null == methodHookParam.args[3]));
        }
        if (i == 2) {
            Log.d("Mulin", "Fake Location listener");
            try {
                methodHookParam.args[1] = new FakeLocationListener((LocationListener) methodHookParam.args[1]);
            } catch(Exception e){
                e.printStackTrace();
            }

            Log.d("Mulin", "Complete");
//            }
        }

    }

    @Override
    public Member getCallable(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method m : methods) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (m.getName().equals("requestLocationUpdates") && m.getParameterCount() == 4) {
                    Log.d("Mulin", "parameters types are:"+m.getParameterTypes()[0].getSimpleName()
                    +" "+m.getParameterTypes()[1].getSimpleName() + " "+m.getParameterTypes()[2].getSimpleName() +" "+m.getParameterTypes()[3].getSimpleName());
                    Log.d("Mulin", "getCallable: requestLocationUpdates");
                    m.setAccessible(true);
                    return m;
                }
                if (m.getName().equals("requestLocationUpdates")){
                    Log.d("Mulin", "the para length is "+m.getParameterCount());
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
    public String toString(){ return "ACCESS_FINE_LOCATION";}
}
