package sg.edu.smu.xposedmoduledemo.xposed;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import sg.edu.smu.xposedmoduledemo.SMUAppOps;

//public class XAppOpsHook implements XHook{
//    public Member getMethod(Class<?> cls) {
//        Member mthd = null;
//        Constructor<?>[] cons = cls.getDeclaredConstructors();
//        for (Constructor<?> con : cons) {
//            Log.i("Mulin", "This is a constructor " + cons.length);
//            if (con.getParameterTypes().length == 2) {
//                Log.d("Mulin", "length is 2");
//                con.setAccessible(true);
//                mthd = con;
//            }
//        }
//
//        return mthd;
//    }
//
//    public XC_MethodHook getCallback() {
//
//        return new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                Log.d("Mulin", "beforeHookedMethod: XAppOpsHook");
//            }
//
//            /* access modifiers changed from: protected */
//            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
//                Log.d("Mulin", "afterHookedMethod: XAppOpsHook");
//                new Thread((Runnable) new SMUAppOps(param.thisObject)).start();
//
//            }
//        };
//    }
//
//    @Override
//    public String getClassName() {
//        return "android.app.AppOpsManager";
//    }
//
//
//}