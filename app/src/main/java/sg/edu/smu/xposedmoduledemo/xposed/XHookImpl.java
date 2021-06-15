package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.DBHelper;
import sg.edu.smu.xposedmoduledemo.MainActivity;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam, TextReceiver textReceiver) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
        this.mTextReceiver = textReceiver;
    }
    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam, ButtonSingleton buttonSingleton) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
        this.buttonSingleton = buttonSingleton;
    }

    @Override
    public XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {


                Object instance = param.thisObject;
                Object[] args = param.args;

                if (XHookImpl.this.prov.shouldHook(instance, args)) {
                    //dynamically register the broadcast
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("Click_Event");
                    AndroidAppHelper.currentApplication().registerReceiver(mTextReceiver, filter);

                    Context vxContext = AndroidAppHelper.currentApplication().getApplicationContext().createPackageContext("sg.edu.smu.xposedmoduledemo", 0);
                    pref = vxContext.getSharedPreferences("permission_info",Context.MODE_PRIVATE);
                    pref2 = vxContext.getSharedPreferences("button_permission_info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor pref2_editor = pref2.edit();
                    pref2_editor.putString("sg.edu.smu.permissionrequestappREAD_CONTACTS2131230807","2");
                    pref2_editor.putString("streetdirectory.mobileACCESS_FINE_LOCATION2131230754","2");
                    pref2_editor.apply();
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
//                    XposedHelpers.findAndHookMethod(buttonClass,
//                            loadPackageParam.classLoader,"onClick", View.class,XButtonHook.getCallback());
//                    Log.d("Mulin", "button id is " + packageName+prov.toString()+ButtonSingleton.getInstance().getId());
//                    result[0] = Integer.parseInt(pref2.getString(packageName+prov.toString()+ButtonSingleton.getInstance().getId(),
//                            pref.getString(packageName+prov.toString(),"0")));
//
//                    Class c = Class.forName("sg.edu.smu.permissionrequestapp.MainActivity$1");
//                    Method method = c.getDeclaredMethod("onClick",View.class);
//                    XposedBridge.hookMethod(method, XButtonHook.getCallback());

//                    XButtonHook xbuttonHook = new XButtonHook();

//                    XposedHelpers.findAndHookMethod(XposedHelpers.findClass(buttonClass,loadPackageParam.classLoader),
//                            "onClick", View.class, XButtonHook.getCallback());
                    result[0] = Integer.parseInt(pref2.getString(packageName+prov.toString()+ButtonSingleton.getInstance().getId(),
                            pref.getString(packageName+prov.toString(),"0")));


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


