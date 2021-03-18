package sg.edu.smu.xposedmoduledemo;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.Lists;

import java.lang.reflect.Member;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.hooks.Contacts;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;
import sg.edu.smu.xposedmoduledemo.xposed.XAppOpsHook;
import sg.edu.smu.xposedmoduledemo.xposed.XButtonHook;
import sg.edu.smu.xposedmoduledemo.xposed.XContextHook;
import sg.edu.smu.xposedmoduledemo.xposed.XHook;
import sg.edu.smu.xposedmoduledemo.xposed.XHookImpl;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HelloXp implements IXposedHookLoadPackage {
    private final List<HookTemplate> hooks = Lists.newArrayList();

    public HelloXp() {
        loadAllHooks();
    }

    private void loadAllHooks() {
        for (HookTemplate prov : new HookTemplate[]{new Contacts()}) {
            this.hooks.add(prov);

        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XHook hook;
        String className;
        Member m;
        XButtonHook xbuttonHook;

//        XContextHook.hook(loadPackageParam.classLoader);

        for (HookTemplate prov : this.hooks) {
            hook = new XHookImpl(prov, loadPackageParam.packageName,loadPackageParam);
            Log.d("Mulin", "handleLoadPackage: "+loadPackageParam.packageName);
            m = hook.getMethod(loadPackageParam.classLoader.loadClass(hook.getClassName()));
            XposedBridge.hookMethod(m, hook.getCallback());
        }

//        xbuttonHook = new XButtonHook();
//        XposedHelpers.findAndHookMethod("sg.edu.smu.permissionrequestapp.MainActivity$1",
//                loadPackageParam.classLoader,"onClick",View.class,xbuttonHook.getCallback());

//        final Class ananName = XposedHelpers.findClass("sg.edu.smu.permissionrequestapp.MainActivity$1", loadPackageParam.classLoader);
//        XposedHelpers.findAndHookMethod(ananName, "onClick", Context.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                Log.d("Mulin", "beforeHookedMethod: is executed");
//                Object view = param.args[0];
//                View v  = (Button) view;
//                Log.d("Mulin", ""+v);
//
//            }
//        });


//Demo
//        final Class className = XposedHelpers.findClass("sg.edu.smu.permissionrequestapp.ContactUtils", loadPackageParam.classLoader);
//        XposedHelpers.findAndHookMethod(className, "getAllContacts", Context.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("app is trying to get your contact");
//            }
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                MyContacts temp = new MyContacts();
//                temp.name = "FakeName";
//                temp.phone = "00000000";
//                temp.note = "Fake successful";
//                param.setResult(temp);
//                Log.d("Mulin", "Fake successful");
//            }
//        });

    }
}
