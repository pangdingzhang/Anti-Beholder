package sg.edu.smu.xposedmoduledemo.hooks;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.util.Util;

public class Location implements HookTemplate{
    private int result;

    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        if (this.result == 2) {
            android.location.Location l = new android.location.Location("passive");
            Util.modifyLocation(l);
            methodHookParam.setResult(l);
        }

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        this.result = i;
    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            return cls.getMethod("getLastLocation", null);
        } catch (NoSuchMethodException e) {
            return null;
        }

    }

    @Override
    public String getClassName() {
        return "android.location.LocationManager";
    }

    @Override
    public int getOp() {
        return 0;
    }

    @Override
    public boolean shouldHook(Object obj, Object... objArr) {
        return true;
    }

    @Override
    public String toString(){ return "getLastLocation";}
}
