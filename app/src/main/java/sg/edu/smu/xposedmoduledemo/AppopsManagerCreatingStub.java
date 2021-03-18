package sg.edu.smu.xposedmoduledemo;

import android.app.AppOpsManager;
import android.content.Context;

import androidx.core.app.AppOpsManagerCompat;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class AppopsManagerCreatingStub {

    Object manager;



    public AppopsManagerCreatingStub(Context c) {
        //Use with getSystemService to retrieve a AppOpsManager for tracking application operations on the device.
        this.manager = c.getSystemService(c.APP_OPS_SERVICE);
    }

    public AppopsManagerCreatingStub(Object manager2) {
        if (manager2 == null) {
            throw new RuntimeException("not allowed null here");
        }
        this.manager = manager2;
    }


    public void setMode(int op, int uid, String packageName, int mode) throws IOException {
        try {
            this.manager.getClass().getMethod("setMode", Integer.TYPE, Integer.TYPE, String.class, Integer.TYPE).invoke(this.manager, Integer.valueOf(op), Integer.valueOf(uid), packageName, Integer.valueOf(mode));
        } catch (Exception e) {
        }
    }


    public int unsafeCheckOp(int op, int uid, String packageName) {
        try {
            this.manager.getClass().getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class).invoke(this.manager, Integer.valueOf(op), Integer.valueOf(uid), packageName);
        } catch (Exception e) {
        }
        return 0;
    }


    public ArrayList<PackageSettings> getAllPackages() throws IOException {
        try {
            Object packageOpsList = this.manager.getClass().getMethod("getPackagesForOps", int[].class).invoke(this.manager, null);
            ArrayList<PackageSettings> res = new ArrayList<>();
            Method get = packageOpsList.getClass().getMethod("get", Integer.TYPE);
            int size = (Integer) packageOpsList.getClass().getMethod("size", new Class[0]).invoke(packageOpsList, new Object[0]);
            for (int i = 0; i < size; i++) {
                Object currEntry = get.invoke(packageOpsList, Integer.valueOf(i));
                String packageName = (String) currEntry.getClass().getMethod("getPackageName", new Class[0]).invoke(currEntry, new Object[0]);
                int uid = (Integer) currEntry.getClass().getMethod("getUid", new Class[0]).invoke(currEntry, new Object[0]);
                Object opEntries = currEntry.getClass().getMethod("getOps", new Class[0]).invoke(currEntry, new Object[0]);
                Method opGet = opEntries.getClass().getMethod("get", Integer.TYPE);
                int opLen = (Integer) opEntries.getClass().getMethod("size", new Class[0]).invoke(opEntries, new Object[0]);
                for (int j = 0; j < opLen; j++) {
                    Object currOpEntry = opGet.invoke(opEntries, j);
                    res.add(new PackageSettings(uid, (Integer) currOpEntry.getClass().getMethod("getOp", new Class[0]).invoke(currOpEntry, new Object[0]), packageName, ((Integer) currOpEntry.getClass().getMethod("getMode", new Class[0]).invoke(currOpEntry, new Object[0])).intValue()));
                }
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }

}
