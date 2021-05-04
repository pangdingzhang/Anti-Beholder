package sg.edu.smu.xposedmoduledemo.hooks;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class GoogleLocationHook implements HookTemplate{

    private static final String className = "com.google.android.gms.location.LocationResult";
    private static int lastRes = 2;
    private static final int opNum = 1;
    private int result;

    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        if (this.result == 3) {
            List<android.location.Location> res = new ArrayList<>();
            res.add(new FakeLocation());
            methodHookParam.setResult(res);
        } else if (this.result == 1) {
            methodHookParam.setResult((Object) null);
        }

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        this.result = i;

    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            return cls.getMethod("getLocations", new Class[0]);
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
        return 0;
    }

    @Override
    public boolean shouldHook(Object obj, Object... objArr) {
        return true;

    }
    @Override
    public String toString(){ return "GoogleLocation";}
}
