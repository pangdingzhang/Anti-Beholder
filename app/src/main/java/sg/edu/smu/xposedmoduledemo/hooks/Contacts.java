package sg.edu.smu.xposedmoduledemo.hooks;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.actions.SearchIntents;
import de.robv.android.xposed.XC_MethodHook;

import java.lang.reflect.Member;



public class Contacts implements HookTemplate {
    private static int msgsOpNum = 4;
    private int result;

    @Override
    public Member getCallable(Class<?> cls) {
        try {
            Log.d("Mulin", "getCallable: "+cls.getMethod(SearchIntents.EXTRA_QUERY, Uri.class, String[].class, String.class, String[].class, String.class));

            return cls.getMethod(SearchIntents.EXTRA_QUERY, Uri.class, String[].class, String.class, String[].class, String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override // org.synergylabs.pmpandroid.hooks.HookInformationProvider
    public boolean shouldHook(Object instance, Object... args) {
        boolean z = false;
        Uri uri = (Uri) args[0];
        if (uri.compareTo(ContactsContract.CommonDataKinds.Email.CONTENT_URI) == 0 || uri.compareTo(ContactsContract.CommonDataKinds.Phone.CONTENT_URI) == 0 || uri.compareTo(ContactsContract.Contacts.CONTENT_URI) == 0 || uri.compareTo(ContactsContract.PhoneLookup.CONTENT_FILTER_URI) == 0 || uri.compareTo(ContactsContract.Data.CONTENT_URI) == 0) {
            z = true;
        }
        Log.d("Mulin", "shouldHook: "+z);
        return Boolean.valueOf(z).booleanValue();
    }

    @Override
    public String getClassName() {
        return "android.content.ContentResolver";
    }

    @Override
    public int getOp() {
        return msgsOpNum;
    }

    @Override
    public void beforeInvocation(XC_MethodHook.MethodHookParam params, int result2) {
        this.result = result2;
    }

    @Override
    public void afterInvocation(XC_MethodHook.MethodHookParam params) {
        if (this.result == 2) {
            params.setResult(new DummyCursor((Cursor) params.getResult()));
        }
    }

    public String toString() {
        return "READ_CONTACTS";
    }

}
