package sg.edu.smu.xposedmoduledemo.hooks;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public class CheckPermission implements HookTemplate{
    int result;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.d("Mulin", "after checkPermission");
        if (result == 0) {
            Log.d("Mulin", "checkPermission afterInvocation: result is 0");
            methodHookParam.setResult(PackageManager.PERMISSION_GRANTED);
        } else if (result == 1) {
            Log.d("Mulin", "checkPermission afterInvocation: result is -1");
            methodHookParam.setResult(PackageManager.PERMISSION_DENIED);
        } else {
            methodHookParam.setResult(PackageManager.PERMISSION_GRANTED);
            Log.d("Mulin", "checkPermission afterInvocation: other cases set result as 0");

        }
    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        Log.d("Mulin", "before checkPermission");
        this.result = i;
    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            Log.d("Mulin", "getCallable: checkPermission");
            return cls.getMethod("checkPermission", String.class, int.class, int.class);
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getClassName() {
        return "android.app.Activity";
    }

    @Override
    public int getOp() {
        return 0;
    }

    @Override
    public boolean shouldHook(Object obj, Object... objArr) {
        return true;
    }
}
