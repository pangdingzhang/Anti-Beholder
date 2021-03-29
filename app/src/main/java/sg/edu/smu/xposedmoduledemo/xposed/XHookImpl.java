package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.LongDef;

import com.google.common.collect.Maps;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.MyAccessibilityService;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;
import sg.edu.smu.xposedmoduledemo.pojos.*;

public class XHookImpl implements XHook {
//    private static final double TIME_TO_LIVE = 15000.0d;
//    private static final ConcurrentMap<Integer, Integer> cachedResponse = Maps.newConcurrentMap();
//    private static final ConcurrentMap<Integer, Long> lastUpdated = Maps.newConcurrentMap();
    private boolean hasHooked;
    private final String packageName;
    private final HookTemplate prov;
    private XC_LoadPackage.LoadPackageParam loadPackageParam;

    public XHookImpl(HookTemplate prov2, String packageName2, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.prov = prov2;
        this.packageName = packageName2;
        this.loadPackageParam = loadPackageParam;
    }

    @Override
    public XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                String buttonClass = "";
//                Log.d("Mulin", "before stacktrace");
                // Test get stack trace to get the button name
                StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                for(StackTraceElement e : stacktrace){
//                    Log.d("Mulin", e.getClassName()+"   "+e.getMethodName());
                    if (e.getMethodName().equals("onClick")) {
                        buttonClass = e.getClassName();
                        break;
                    }
                }
                XButtonHook xbuttonHook = new XButtonHook();
                XposedHelpers.findAndHookMethod(buttonClass,
                        loadPackageParam.classLoader,"onClick", View.class,XButtonHook.getCallback());

//                Activity launchedUI = (Activity) param.thisObject;
//                Context context = launchedUI.getApplicationContext();
//                Context context = (Context) AndroidAppHelper.currentApplication();
//                String buttonText = xbuttonHook.getButtontext();
//                int duration = Toast.LENGTH_LONG;
//
//                Toast toast = Toast.makeText(context, buttonText, duration);
//                toast.show();

                Log.d("Mulin", "beforeHookedMethod: "+packageName+"is trying to obtain "+prov);
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
                    result[0] = 3; // 3 means fake
//                    Thread th = new Thread() {
//                        /* class org.synergylabs.pmpandroid.hooks.xposed.XHookImpl.AnonymousClass1.AnonymousClass1 */
//
//                        public void run() {
//                            Socket s = null;
//                            int tries = 0;
//                            while (true) {
//                                if (tries >= 10) {
//                                    break;
//                                }
//                                try {
//                                    s = new Socket("localhost", (int) PMPService.SERVICEPORT);
//                                    break;
//                                } catch (IOException e) {
//                                    tries++;
//                                    try {
//                                        sleep((long) 100);
//                                    } catch (InterruptedException e2) {
//                                    }
//                                }
//                            }
//                            if (s != null) {
//                                try {
//                                    if (!s.isClosed()) {
//                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
//                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//                                        oos.writeObject(req);
////                                        result[0] = ((PermissionModeResponse) ois.readObject()).getResponse();
//                                        result[0] = 3;
//                                        XHookImpl.lastUpdated.put(Integer.valueOf(XHookImpl.this.prov.getOp()), Long.valueOf(currTime));
//                                        XHookImpl.cachedResponse.put(Integer.valueOf(XHookImpl.this.prov.getOp()), Integer.valueOf(result[0]));
//                                        s.close();
//                                        return;
//                                    }
//                                } catch (Throwable e3) {
//                                    Log.e("XHookImpl", "top level exception" + e3.getMessage());
//                                    return;
//                                }
//                            }
//                            Log.e("XHookImpl", "prov for " + XHookImpl.this.packageName + " could not start a connection");
//                        }
//                    };
//                    th.start();
//                    th.join();
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
}


