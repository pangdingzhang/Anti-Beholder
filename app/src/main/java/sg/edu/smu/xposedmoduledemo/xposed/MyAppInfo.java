package sg.edu.smu.xposedmoduledemo.xposed;

import android.graphics.drawable.Drawable;

public class MyAppInfo {
    private Drawable image;
    private String appName;
    private String[] appPermission;
    private String packageName;

    public MyAppInfo(Drawable image, String appName, String[] appPermission) {
        this.image = image;
        this.appName = appName;
        this.appPermission = appPermission;
    }

    public MyAppInfo(){

    }


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String[] getAppPermission() {
        return appPermission;
    }

    public void setAppPermission(String[] appPermission) {
        this.appPermission = appPermission;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
