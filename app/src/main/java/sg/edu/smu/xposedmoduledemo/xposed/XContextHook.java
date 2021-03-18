package sg.edu.smu.xposedmoduledemo.xposed;

import android.content.Context;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class XContextHook {
    private static final String CLASS_NAME = "android.content.Context";
    private static Context CONTEXT;

    public static void hook(ClassLoader classLoader) {
        try {
            Log.d("Mulin", "XContextHook");
            XposedBridge.hookAllConstructors(classLoader.loadClass(CLASS_NAME), new XC_MethodHook() {
                /* class org.synergylabs.pmpandroid.hooks.xposed.XContextHook.AnonymousClass1 */

                /* access modifiers changed from: protected */
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    Log.d("Mulin", "HookConsturctor");
                    if (!(XContextHook.CONTEXT == null && param.thisObject == null)) {
                        Log.d("Mulin", "afterHookedMethod: context");
                        Context unused = XContextHook.CONTEXT = (Context) param.thisObject;
                        Log.d("Mulin", "afterHookedMethod: context"+unused);
                        super.afterHookedMethod(param);

                    }
//                    Context unused = XContextHook.CONTEXT = (Context) param.thisObject;
//                    super.afterHookedMethod(param);
//                    Log.d("Mulin", "afterHookedMethod: context");
                }
            });
        } catch (ClassNotFoundException e) {
            Log.e("XContextHook", "android.content.Context not found");
        }
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
