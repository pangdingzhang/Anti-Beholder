package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AlertDialog;
import android.app.AndroidAppHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.DBHelper;
import sg.edu.smu.xposedmoduledemo.MainActivity;
import sg.edu.smu.xposedmoduledemo.R;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class XHookImpl implements XHook {
//    private static final double TIME_TO_LIVE = 15000.0d;
//    private static final ConcurrentMap<Integer, Integer> cachedResponse = Maps.newConcurrentMap();
//    private static final ConcurrentMap<Integer, Long> lastUpdated = Maps.newConcurrentMap();
    private boolean hasHooked;
    private final String packageName;
    private final HookTemplate prov;
    private XC_LoadPackage.LoadPackageParam loadPackageParam;
    private TextReceiver mTextReceiver;
    private SharedPreferences pref;
    private SharedPreferences pref2;
    private DBHelper dbHelper;
    private ButtonSingleton buttonSingleton;
//    private DecisionReceiver  decisionReceiver;
    private AccessNotificationSingleton accessNotificationSingleton;

    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam){
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
    }

    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam, AccessNotificationSingleton accessNotificationSingleton) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
        this.accessNotificationSingleton = accessNotificationSingleton;
    }
    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam, ButtonSingleton buttonSingleton, AccessNotificationSingleton accessNotificationSingleton) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
        this.buttonSingleton = buttonSingleton;
        this.accessNotificationSingleton = accessNotificationSingleton;

    }

    @Override
    public XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            public void beforeHookedMethod(MethodHookParam param) throws Throwable {


                Object instance = param.thisObject;
                Object[] args = param.args;

                if (XHookImpl.this.prov.shouldHook(instance, args)) {

                    Context vxContext = AndroidAppHelper.currentApplication().getApplicationContext().createPackageContext("sg.edu.smu.xposedmoduledemo", 0);
                    pref = vxContext.getSharedPreferences("permission_info",Context.MODE_PRIVATE);
                    pref2 = vxContext.getSharedPreferences("button_permission_info",Context.MODE_PRIVATE);
//                    SharedPreferences.Editor pref2_editor = pref2.edit();
//                    pref2_editor.putString("sg.edu.smu.permissionrequestappREAD_CONTACTS2131230807","2");
//                    pref2_editor.putString("streetdirectory.mobileACCESS_FINE_LOCATION2131230754","2");
//                    pref2_editor.apply();
                    dbHelper = new DBHelper(vxContext, "Permission.db", null, 1);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //store the permission access record into db
                    values.put("package_name", packageName);
                    values.put("permission", prov.toString());
                    values.put("time", (System.currentTimeMillis()));
                    db.insert("permission_db", null, values);
                    values.clear();

                    Log.d("Mulin", "beforeHookedMethod: " + packageName + "is trying to obtain " + prov);
                    final int[] result = {0};
//                    result[0] = Integer.parseInt(pref.getString(packageName+prov.toString(),"0"));

                    String buttonClass = "";

                    // Use stack trace to get the button clicking event
                    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                    for(StackTraceElement e : stacktrace){
//                    Log.d("Mulin", e.getClassName()+"   "+e.getMethodName());
                        if (e.getMethodName().equals("onClick")) {
                            buttonClass = e.getClassName();
                            break;
                        }
                    }
                    Log.d("Mulin", "button class is "+buttonClass);
//                    result[0] = Integer.parseInt(pref2.getString(packageName+prov.toString()+ButtonSingleton.getInstance().getId(),
//                            pref.getString(packageName+prov.toString(),"0")));
//
                    Log.d("Mulin", "try to find permission of button "+packageName+prov.toString()+ButtonSingleton.getInstance().getId());

                    result[0] = Integer.parseInt(pref2.getString(packageName+prov.toString()+ButtonSingleton.getInstance().getId(),
                            pref.getString(packageName+prov.toString(),"0")));


//                    Intent intent = new Intent("SEND_TO_SERVICE");
//                    Bundle bundle=new Bundle();
//                    bundle.putString("PKG_NAME",packageName);
//                    bundle.putString("PERMISSION_NAME", prov.toString());
//
//                    intent.putExtra("Bundle", bundle);
//                    vxContext.sendBroadcast(intent);


//                    if (decisionReceiver == null) {
//                        decisionReceiver = new DecisionReceiver(accessNotificationSingleton);
//                    }
//                    IntentFilter intentFilter = new IntentFilter("ACTION");
//                    vxContext.registerReceiver(decisionReceiver, intentFilter);
//                    Log.d("Mulin", "XHookImle "+packageName+XHookImpl.this.prov.toString());
//                    Thread.sleep(5000);
//                    Log.d("Mulin", "in impl class, the singleton id is "+accessNotificationSingleton);
//                    result[0] = Integer.parseInt(accessNotificationSingleton.getDecision().get(packageName+XHookImpl.this.prov.toString()));

                    Log.d("Mulin", "result 0 is : "+result[0]);
                    XHookImpl.this.hasHooked = true;
                    XHookImpl.this.prov.beforeInvocation(param, result[0]);

                }
            }


            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                if (XHookImpl.this.hasHooked) {
                    XHookImpl.this.prov.afterInvocation(param);
                }

                Log.d("Mulin", "Hook Complete\n-----------------------");

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

}


