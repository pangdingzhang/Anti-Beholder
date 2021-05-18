package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.MainActivity;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

import java.lang.reflect.Member;

public class XHookImpl implements XHook {
//    private static final double TIME_TO_LIVE = 15000.0d;
//    private static final ConcurrentMap<Integer, Integer> cachedResponse = Maps.newConcurrentMap();
//    private static final ConcurrentMap<Integer, Long> lastUpdated = Maps.newConcurrentMap();
    private boolean hasHooked;
    private final String packageName;
    private final HookTemplate prov;
    private XC_LoadPackage.LoadPackageParam loadPackageParam;
    private Toast toast;
    private SharedPreferences pref;

    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
    }

    @Override
    public XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Context vxContext = AndroidAppHelper.currentApplication().getApplicationContext().createPackageContext("sg.edu.smu.xposedmoduledemo", 0);
                pref = vxContext.getSharedPreferences("permission_info",Context.MODE_PRIVATE);

//                Context context =  AndroidAppHelper.currentApplication();
//                String buttonText = xbuttonHook.getButtontext();
//                Log.d("Mulin", "in XHookImple the button text is "+buttonText);

//                showAToast(buttonText);
//                Toast.makeText(context, buttonText, Toast.LENGTH_SHORT).show();

//                XposedHelpers.findAndHookMethod(buttonClass,
//                        loadPackageParam.classLoader, "onClick", View.class, new XC_MethodHook() {
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                                Object view = param.args[0];
//                                Button v  = (Button) view;
//                                Log.d("Mulin", "You just clicked "+ v.getText().toString());
//                                Context context = (Context) AndroidAppHelper.currentApplication();
//                            }
//                        });

//                XposedHelpers.findAndHookMethod(View.class ,"setOnClickListener", View.OnClickListener.class, new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        View view = (View)param.thisObject;
//                        //ImageView
//                        String Str = null;
//                        if (view instanceof TextView){//也有可能是ImageView，所以得判断一下
//                            Str = ((TextView)view).getText().toString();
//                        }
//                        int btnId = view.getId();
//                        Log.i("ButtonInfo", Str + " " + btnId);
//
//                    }
//                });


                Log.d("Mulin", "beforeHookedMethod: " + packageName + "is trying to obtain " + prov);
//                super.beforeHookedMethod(param);
//                final long currTime = System.currentTimeMillis();
//                Long lastUpdatedTime = (Long) XHookImpl.lastUpdated.get(Integer.valueOf(XHookImpl.this.prov.getOp()));
                Object instance = param.thisObject;
                Object[] args = param.args;
//                final PermissionModeRequest req = new PermissionModeRequestImpl(XHookImpl.this.prov.getOp(), Thread.currentThread().getStackTrace(), Process.myUid());
//                if (lastUpdatedTime != null && ((double) (currTime - lastUpdatedTime.longValue())) < XHookImpl.TIME_TO_LIVE && XHookImpl.cachedResponse.containsKey(Integer.valueOf(XHookImpl.this.prov.getOp()))) {
//                    XHookImpl.this.hasHooked = true;
//                    XHookImpl.this.prov.beforeInvocation(param, ((Integer) XHookImpl.cachedResponse.get(Integer.valueOf(XHookImpl.this.prov.getOp()))).intValue());
//                }
                if (XHookImpl.this.prov.shouldHook(instance, args)) {
                    final int[] result = {0};
//                    result[0] = 3; // 3 means fake
                    result[0] = Integer.parseInt(pref.getString(packageName+prov.toString(),"0"));
                    XHookImpl.this.hasHooked = true;
                    XHookImpl.this.prov.beforeInvocation(param, result[0]);

                    String buttonClass = "";
                    // Test get stack trace to get the button name
                    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                    for(StackTraceElement e : stacktrace){
//                    Log.d("Mulin", e.getClassName()+"   "+e.getMethodName());
                        if (e.getMethodName().equals("onClick")) {
                            buttonClass = e.getClassName();
                            break;
                        }
                    }
//                    XButtonHook xbuttonHook = new XButtonHook();
//                    XposedHelpers.findAndHookMethod(buttonClass,
//                            loadPackageParam.classLoader,"onClick", View.class,xbuttonHook.getCallback());
                }
            }

            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                if (XHookImpl.this.hasHooked) {
                    XHookImpl.this.prov.afterInvocation(param);
                }
                Log.d("Mulin", "Hook Complete");

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

    public void showAToast (String message){
        if (toast != null) {
            toast.cancel();
        }
        Context context = (Context) AndroidAppHelper.currentApplication();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public StackTraceElement[] reverseStackTrace(StackTraceElement[] original){
        for (int k = 0; k < original.length/2; k++) {
            StackTraceElement temp = original[k];
            original[k] = original[original.length-(1+k)];
            original[original.length-(1+k)] = temp;
        }
        return original;
    }
}


