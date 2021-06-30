package sg.edu.smu.xposedmoduledemo.hooks;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public class CheckSelfPermission implements HookTemplate{
    int result;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.d("Mulin", "after NoteProxyOp");
        if (result == 0) methodHookParam.setResult(PackageManager.PERMISSION_GRANTED);
        else if (result == 1) {
            Log.d("Mulin", "afterInvocation: result is 1");
            methodHookParam.setResult(PackageManager.PERMISSION_DENIED);
        }
    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        Log.d("Mulin", "before NoteProxyOp");
        this.result = i;
    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            Log.d("Mulin", "getCallable: checkSelfPermission");
            return cls.getMethod("checkSelfPermission", Context.class, String.class);
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getClassName() {
        return "androidx.core.content.ContextCompat";
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
