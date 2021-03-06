package sg.edu.smu.xposedmoduledemo;

import android.util.Log;

import com.google.common.collect.Lists;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import sg.edu.smu.xposedmoduledemo.hooks.CheckPermission;
import sg.edu.smu.xposedmoduledemo.hooks.onRequestPermissionsResult;
import sg.edu.smu.xposedmoduledemo.hooks.CheckSelfPermission;
import sg.edu.smu.xposedmoduledemo.hooks.Contacts;
import sg.edu.smu.xposedmoduledemo.hooks.FineLocationHook;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

import sg.edu.smu.xposedmoduledemo.hooks.LocationUpdate;
import sg.edu.smu.xposedmoduledemo.xposed.AccessNotificationSingleton;
import sg.edu.smu.xposedmoduledemo.xposed.AppOpsHookProvider;
import sg.edu.smu.xposedmoduledemo.xposed.ButtonHookProvider;
import sg.edu.smu.xposedmoduledemo.xposed.ButtonSingleton;
import sg.edu.smu.xposedmoduledemo.xposed.XButtonHook;
import sg.edu.smu.xposedmoduledemo.xposed.XHook;
import sg.edu.smu.xposedmoduledemo.xposed.XHookImpl;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HelloXp implements IXposedHookLoadPackage {
    private final List<HookTemplate> hooks = Lists.newArrayList();
    private AccessNotificationSingleton accessNotificationSingleton = new AccessNotificationSingleton();
    private ButtonSingleton buttonSingleton = new ButtonSingleton();
    private final Map<String, String> buttonClasses = new HashMap<>();
    private final List<HookTemplate> permissionHooks = Lists.newArrayList();
    private final Set<String> activities = new HashSet<>();

    public HelloXp() {
        loadAllHooks();
        loadButtons();
        loadPermissionHooks();
        loadActivies();
    }

    private void loadAllHooks() {
        for (HookTemplate priv : new HookTemplate[]{new Contacts(), new FineLocationHook(), new LocationUpdate(), new XButtonHook()}) {
            this.hooks.add(priv);
        }
    }

    private void loadPermissionHooks(){
        for (HookTemplate priv : new HookTemplate[]{new CheckPermission(), new onRequestPermissionsResult()}) {
            this.permissionHooks.add(priv);
        }
    }

    private void loadButtons(){
        buttonClasses.put("sg.edu.smu.permissionrequestapp.MainActivity$1","READ_CONTACTS");
        buttonClasses.put("sg.edu.smu.permissionrequestapp.MainActivity$2","READ_CONTACTS");
        buttonClasses.put("streetdirectory.mobile.gis.maps.MapView$4", "ACCESS_FINE_LOCATION");
    }

    private void loadActivies(){
        activities.add("streetdirectory.mobile.modules.map.MapActivity");
        activities.add("sg.edu.smu.permissionrequestapp.MainActivity");
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Member m;


        for (HookTemplate priv : this.permissionHooks) {
            AppOpsHookProvider appOpsHookProvider = new AppOpsHookProvider(priv, loadPackageParam.packageName);
            // activity class name will be gotten from static analysis
            if (priv instanceof onRequestPermissionsResult){
                for (String activityClassName : activities){
                    if(activityClassName.startsWith(loadPackageParam.packageName)){
                        Log.d("Mulin", activityClassName+" is hooked");
                        Member method = appOpsHookProvider.getMethod(loadPackageParam.classLoader.loadClass(activityClassName));
                        XposedBridge.hookMethod(method, appOpsHookProvider.getCallback());
                    }
                }
            } else {
                Member method = appOpsHookProvider.getMethod(loadPackageParam.classLoader.loadClass(appOpsHookProvider.getClassName()));
                XposedBridge.hookMethod(method, appOpsHookProvider.getCallback());
            }
//            try{
//                Member method = appOpsHookProvider.getMethod(loadPackageParam.classLoader.loadClass(appOpsHookProvider.getClassName()));
//                XposedBridge.hookMethod(method, appOpsHookProvider.getCallback());
//            }catch (ClassNotFoundException e){
//                Need to print stack trace
//            }
        }

        for (HookTemplate priv : this.hooks) {
//            hook = new XHookImpl(prov, loadPackageParam.packageName,loadPackageParam,textReceiver);

            Log.d("Mulin", "handleLoadPackage: "+loadPackageParam.packageName);

            // here button hook is treated differently bcz it get class name from a different place
            if (priv instanceof XButtonHook) {
                Log.d("Mulin", "instance of button hook");
                ButtonHookProvider hook = new ButtonHookProvider(priv, loadPackageParam.packageName);
                for(String buttonClass : buttonClasses.keySet()){
                    //only load buttons of current package
                    if (buttonClass.startsWith(loadPackageParam.packageName)){
                        hook.setPermission((String) buttonClasses.get(buttonClass));
                        m = hook.getMethod(loadPackageParam.classLoader.loadClass(buttonClass));
                        XposedBridge.hookMethod(m, hook.getCallback());
                    }

                }
            } else{
                XHook hook = new XHookImpl(priv, loadPackageParam.packageName,loadPackageParam,buttonSingleton,accessNotificationSingleton);
                m = hook.getMethod(loadPackageParam.classLoader.loadClass(hook.getClassName()));
                XposedBridge.hookMethod(m, hook.getCallback());
            }


            Log.d("Mulin", "handleLoadPackage: once");
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

    }
}
