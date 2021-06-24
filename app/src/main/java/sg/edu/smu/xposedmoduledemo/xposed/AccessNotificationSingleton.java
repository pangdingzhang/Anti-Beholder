package sg.edu.smu.xposedmoduledemo.xposed;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class AccessNotificationSingleton implements Serializable {
    private static AccessNotificationSingleton instance;
    private String pkgName;
    private String permission;
    private int buttonId;
    private HashMap<String, String> decision = new HashMap<>();
    private HashMap<String, Integer> LastAccessTime = new HashMap<>();


    public synchronized static AccessNotificationSingleton getInstance(){
        if (instance == null){
            instance = new AccessNotificationSingleton();

        }
        Log.d("Mulin", "in singleton, the singleton id is "+instance);
        return instance;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    public HashMap<String, String> getDecision() {
        return decision;
    }

    public void setDecision(HashMap<String, String> decision) {
        this.decision = decision;
    }

    public HashMap<String, Integer> getLastAccessTime() {
        return LastAccessTime;
    }

    public void setLastAccessTime(HashMap<String, Integer> lastAccessTime) {
        LastAccessTime = lastAccessTime;
    }
}
