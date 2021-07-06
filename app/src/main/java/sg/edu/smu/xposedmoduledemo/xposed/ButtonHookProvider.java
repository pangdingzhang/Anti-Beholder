package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

public class ButtonHookProvider implements XHook {
    private  HookTemplate prov;
    private  String packageName;
    private SharedPreferences pref2;
    private BroadcastReceiver decisionReceiver;
    private String permission;

    public ButtonHookProvider(HookTemplate prov, String packageName){
        this.prov = prov;
        this.packageName = packageName;

    }
    @Override
    public XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ButtonHookProvider.this.prov.beforeInvocation(param, 0);
                Context vxContext = AndroidAppHelper.currentApplication().getApplicationContext().createPackageContext("sg.edu.smu.xposedmoduledemo", 0);

                pref2 = vxContext.getSharedPreferences("button_permission_info",Context.MODE_PRIVATE);
                SharedPreferences.Editor pref2_editor = pref2.edit();
                String decision = pref2.getString(packageName+ButtonHookProvider.this.toString()+ButtonSingleton.getInstance().getId(), null);
                if(null == decision){

                    Intent intent = new Intent("SEND_TO_SERVICE");
                    Bundle bundle=new Bundle();
                    bundle.putString("PKG_NAME",packageName);
                    bundle.putString("PERMISSION_NAME", ButtonHookProvider.this.toString());
                    intent.putExtra("Bundle", bundle);
                    vxContext.sendBroadcast(intent);
                    if (decisionReceiver == null) {
                        decisionReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String action = intent.getAction();
                                Log.d("Mulin", "Decision receiver - action received");
                                if(action.equals("ACTION")) {
                                    String temp = intent.getStringExtra("Decision");
                                    String key = intent.getStringExtra("Key");
                                    switch (temp) {
                                        case "Allow":
                                            pref2_editor.putString(packageName + ButtonHookProvider.this.toString() + ButtonSingleton.getInstance().getId(), "0");
                                            Log.d("Mulin", "just put "+packageName + ButtonHookProvider.this.toString() + ButtonSingleton.getInstance().getId());
                                            break;
                                        case "Deny":
//
                                            pref2_editor.putString(packageName + ButtonHookProvider.this.toString() + ButtonSingleton.getInstance().getId(), "1");
                                            break;
                                        case "Fake":
                                            pref2_editor.putString(packageName + ButtonHookProvider.this.toString() + ButtonSingleton.getInstance().getId(), "2");
                                            break;
                                    }
                                    pref2_editor.apply();
                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.cancel(1);
                                }
                            }
                        };
                    }
                    HandlerThread handlerThread = new HandlerThread("ht");
                    handlerThread.start();
                    Looper looper = handlerThread.getLooper();
                    Handler handler = new Handler(looper);

                    IntentFilter intentFilter = new IntentFilter("ACTION");
//                    vxContext.registerReceiver(decisionReceiver, intentFilter);
                    vxContext.registerReceiver(decisionReceiver, intentFilter, null, handler);
                    Log.d("Mulin", "sleep begin");
                    Thread.sleep(4000);
                    Log.d("Mulin", "sleep end");
                }
            }


        };
    }

    @Override
    public Member getMethod(Class<?> cls) {
        return this.prov.getCallable(cls);
    }

    @Override
    public String getClassName() {
        return this.prov.getClassName();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return getPermission();
    }
}
