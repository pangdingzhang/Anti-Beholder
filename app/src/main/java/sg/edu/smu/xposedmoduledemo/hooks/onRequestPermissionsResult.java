package sg.edu.smu.xposedmoduledemo.hooks;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public class onRequestPermissionsResult implements HookTemplate{
    int result;
    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.d("Mulin", "afterInvocation"+((int[]) methodHookParam.args[2])[0]);

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        Log.d("Mulin", "before onRequestPermissionsResult");
        this.result = i;
    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            Log.d("Mulin", "getCallable: onRequestPermissionsResult");
            return cls.getMethod("onRequestPermissionsResult", int.class, String[].class, int[].class);
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getClassName() {
        return "streetdirectory.mobile.modules.map.MapActivity";
//        return "sg.edu.smu.permissionrequestapp.MainActivity";
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
