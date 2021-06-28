package sg.edu.smu.xposedmoduledemo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.smu.xposedmoduledemo.xposed.MyAppInfo;

public class ScanTool {
    static  String TAG = "ScanTool";
    public static List<MyAppInfo> mLocalInstallApps = null;

    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        List<String> dangerousPermissions;
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //filter the system app
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }
                MyAppInfo myAppInfo = new MyAppInfo();

                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setAppName((String) packageManager.getApplicationLabel(packageInfo.applicationInfo));
                myAppInfo.setPackageName(packageInfo.packageName);
                myAppInfo.setUid(packageInfo.applicationInfo.uid);
                if (packageInfo.requestedPermissions != null) {
                    dangerousPermissions = new ArrayList<>();
                    for (String requestedPermission : packageInfo.requestedPermissions) {
                        try {
                            PermissionInfo permissionInfo = packageManager.getPermissionInfo(requestedPermission, 0);
                            switch (permissionInfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) {
                                case PermissionInfo.PROTECTION_DANGEROUS:

                                    dangerousPermissions.add(requestedPermission);
                                    break;
                            }
                        } catch (PackageManager.NameNotFoundException ignored) {
                            // unknown permission
                        }
                    }
//                    myAppInfo.setAppPermission(Arrays.toString(packageInfo.requestedPermissions));
                    myAppInfo.setAppPermission(dangerousPermissions.toArray(new String[0]));
                } else{
                    myAppInfo.setAppPermission(new String[]{"No dangerous permission"});
                }


                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e("TAG","fail to get package info");
        }
        return myAppInfos;
    }

}
