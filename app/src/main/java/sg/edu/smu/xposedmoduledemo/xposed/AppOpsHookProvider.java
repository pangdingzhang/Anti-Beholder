package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.hooks.CheckPermission;
import sg.edu.smu.xposedmoduledemo.hooks.CheckSelfPermission;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

public class AppOpsHookProvider implements XHook{
    private final HookTemplate priv;
    private String packageName;
    private String permission;

    public AppOpsHookProvider(HookTemplate priv, String packageName){
        this.priv = priv;
        this.packageName = packageName;
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
                Context vxContext = AndroidAppHelper.currentApplication().getApplicationContext().createPackageContext("sg.edu.smu.xposedmoduledemo", 0);
                SharedPreferences pref = vxContext.getSharedPreferences("permission_info",Context.MODE_PRIVATE);
                SharedPreferences pref2 = vxContext.getSharedPreferences("button_permission_info",Context.MODE_PRIVATE);
                Log.d("Mulin", "permission is "+param.args[1]);
//                if (priv instanceof CheckSelfPermission){
//                    //for checkSelfPermission, the second argument is permission
//                    permission = ((String) param.args[1]).substring(19);
//                    int result = Integer.parseInt(pref2.getString(packageName+permission+ButtonSingleton.getInstance().getId(),
//                            pref.getString(packageName+permission,"3")));
//                    Log.d("Mulin", "check selfpermission "+permission+" result is"+result);
//                    AppOpsHookProvider.this.priv.beforeInvocation(param, result);

                if(priv instanceof CheckPermission){
                    permission = ((String) param.args[0]).substring(19);
                    int result = Integer.parseInt(pref2.getString(packageName+permission+ButtonSingleton.getInstance().getId(),
                            pref.getString(packageName+permission,"3")));
                    Log.d("Mulin", "checkPermission "+permission+" result is"+result);
                    AppOpsHookProvider.this.priv.beforeInvocation(param, result);
                } else{
                    //for onRequestPermissionResult, the first argument is permission
                    for (int i = 0; i < ((String[]) param.args[1]).length; i++){
                        permission = ((String[]) param.args[1])[i].substring(19);
                        Log.d("Mulin", "onRequestPermissionsResult permission is "+permission);
                        int result = Integer.parseInt(pref2.getString(packageName+permission+ButtonSingleton.getInstance().getId(),
                                pref.getString(packageName+permission,"3")));
                        ((int[]) param.args[2])[i] = result == 1 ? -1: 0;
                        Log.d("Mulin", "onRequestPermissionsResult after modification, the result is "+((int[]) param.args[2])[i]);
                    }
                }


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
