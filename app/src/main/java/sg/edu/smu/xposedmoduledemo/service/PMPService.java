package sg.edu.smu.xposedmoduledemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PMPService extends Service {
    public static final int SERVICEPORT = 10022;

    public void onCreate() {
        super.onCreate();
        Log.d("Mulin", "onCreate: ");
    }
    public IBinder onBind(Intent arg0) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        /* access modifiers changed from: package-private */
        public PMPService getService() {
            return PMPService.this;
        }
    }

}
