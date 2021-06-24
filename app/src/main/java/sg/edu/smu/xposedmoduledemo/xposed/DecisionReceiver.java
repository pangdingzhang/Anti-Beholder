package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DecisionReceiver extends BroadcastReceiver {
    private AccessNotificationSingleton instance;

    public DecisionReceiver() {
    }

    public DecisionReceiver(AccessNotificationSingleton accessNotificationSingleton){
        instance = accessNotificationSingleton;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        String action = intent.getAction();
        Log.d("Mulin", "Decision receiver - action received");
        if(action.equals("ACTION")){
            String temp = intent.getStringExtra("Decision");
            String key = intent.getStringExtra("Key");
            AccessNotificationSingleton instance = (AccessNotificationSingleton) intent.getSerializableExtra("Singleton");
            Log.d("Mulin", "Decision receiver receive "+instance);
            switch (temp){
                case "Allow":
                    instance.getDecision().put(key, "0");
                    break;
                case  "Deny":
                    instance.getDecision().put(key, "1");
                    break;
                case  "Fake":
                    instance.getDecision().put(key, "2");
                    break;
            }
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
    }
}