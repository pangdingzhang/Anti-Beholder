package sg.edu.smu.xposedmoduledemo.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import sg.edu.smu.xposedmoduledemo.xposed.TextReceiver;

public class MyService extends Service {
    private TextReceiver textReceiver;
    private NotificationManager manager;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (textReceiver == null) textReceiver = new TextReceiver();
        IntentFilter intentFilter = new IntentFilter("SEND_TO_SERVICE");
        registerReceiver(textReceiver, intentFilter);
        Log.d("Mulin", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Mulin", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(textReceiver);
        Log.d("Mulin", "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}