package sg.edu.smu.xposedmoduledemo.hooks;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public interface HookTemplate {
    void afterInvocation(XC_MethodHook.MethodHookParam methodHookParam);
    void beforeInvocation(XC_MethodHook.MethodHookParam methodHookParam, int i);
    Member getCallable(Class<?> cls);
    String getClassName();
    int getOp();
    boolean shouldHook(Object obj, Object... objArr);

}
