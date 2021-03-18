package sg.edu.smu.xposedmoduledemo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.Iterator;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    public static MyAccessibilityService mService;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        Log.d("Mulin", "Accessibility Service connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("Mulin", event.toString());
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //界面点击
                if (event.getSource() != null){
                    Log.d("Mulin", event.getSource().getText().toString());
                }

                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //界面文字改动
                break;
        }
    }


    @Override
    public void onInterrupt() {
        Log.d("Mulin", "GG");
    }



    public static boolean isStart() {
        return mService != null;
    }


    public static MyAccessibilityService getInstance() {
        return mService;
    }
}
