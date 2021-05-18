package sg.edu.smu.xposedmoduledemo.hooks;

import androidx.annotation.NonNull;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.util.Util;

public class FineLocationHook implements HookTemplate{
    private static final String className = "android.location.LocationManager";
    private static final int opNum = 1;
    private int result;

    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {


    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        this.result = i;
        if (this.result == 2) {
            android.location.Location l = new android.location.Location("passive");
            Util.modifyLocation(l);
            methodHookParam.setResult(l);
        }

    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            return cls.getMethod("getLastKnownLocation", String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }

    }

    @Override
    public String getClassName() {
        return className;
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
    public String toString() {
        return "ACCESS_FINE_LOCATION";
    }
}
