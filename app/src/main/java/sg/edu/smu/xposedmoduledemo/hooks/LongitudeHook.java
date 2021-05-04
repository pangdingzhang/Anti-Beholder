package sg.edu.smu.xposedmoduledemo.hooks;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public class LongitudeHook implements HookTemplate{
    private static final String className = "android.location.Location";
    private static final int opNum = 1;
    private int result;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        methodHookParam.setResult(103.9879d);
    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {

    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            return cls.getMethod("getLongitude");
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
        return "Longitude";
    }
}
