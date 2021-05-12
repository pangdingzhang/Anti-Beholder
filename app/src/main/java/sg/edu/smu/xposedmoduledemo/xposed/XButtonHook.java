package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XButtonHook {
    public Object view;
    public Button v;
    Toast toast;

    public  XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                view = param.args[0];
                v  = (Button) view;
                Log.d("Mulin", "You just clicked "+ v.getText().toString());
                Context context = (Context) AndroidAppHelper.currentApplication();
//                Toast.makeText(context, v.getText().toString(), Toast.LENGTH_SHORT).show();
                showAToast(v.getText().toString());
            }
        };
    }

    public String getButtontext(){
        return v.getText().toString();
    }

    public void showAToast (String message){
        if (toast != null) {
            toast.cancel();
        }
        Context context = (Context) AndroidAppHelper.currentApplication();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
