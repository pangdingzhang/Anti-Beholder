package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AppOpsManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.SMUAppOps;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

public class AppOpsHookProvider implements XHook{
    private final HookTemplate priv;

    public AppOpsHookProvider(HookTemplate priv){
        this.priv = priv;
        Log.d("Mulin", "AppOpsHookProvider constructor ");
    }

    public Member getMethod(Class<?> cls) {
        return this.priv.getCallable(cls);
    }

    public XC_MethodHook getCallback() {

        return new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("Mulin", "beforeHookedMethod: XAppOpsHook");
                AppOpsHookProvider.this.priv.beforeInvocation(param, 1);
            }

            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                Log.d("Mulin", "afterHookedMethod: XAppOpsHook");
                AppOpsHookProvider.this.priv.afterInvocation(param);

            }
        };
    }

    @Override
    public String getClassName() {
        return this.priv.getClassName();
    }


}
