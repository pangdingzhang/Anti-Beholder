package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import sg.edu.smu.xposedmoduledemo.hooks.HookTemplate;

public class XButtonHook implements HookTemplate {

    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam) {

    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i) {
        Object view = methodHookParam.args[0];
        Button v  = (Button) view;
        Log.d("Mulin", "you just click button "+ v.getId());
        ButtonSingleton.getInstance().setValue(v.getId(),v.getText().toString());
    }

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            return cls.getMethod("onClick", View.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public int getOp() {
        return 0;
    }

    @Override
    public boolean shouldHook(Object obj, Object... objArr) {
        return true;
    }
}
