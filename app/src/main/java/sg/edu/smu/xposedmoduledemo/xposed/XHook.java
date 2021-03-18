package sg.edu.smu.xposedmoduledemo.xposed;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public interface XHook {
    XC_MethodHook getCallback();

    String getClassName();

    Member getMethod(Class<?> cls);

}
