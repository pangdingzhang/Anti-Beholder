package sg.edu.smu.xposedmoduledemo.xposed;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XButtonHook {
    Object view;
    Button v;

    public static XC_MethodHook getCallback() {
        return new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                 Object view = param.args[0];
                 Button v  = (Button) view;
                Log.d("Mulin", "You just clicked "+ v.getText().toString());

            }
        };
    }

    public String getButtontext(){
        return v.getText().toString();
    }

}
