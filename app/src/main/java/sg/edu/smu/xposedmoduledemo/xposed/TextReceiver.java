package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import sg.edu.smu.xposedmoduledemo.MainActivity;
import sg.edu.smu.xposedmoduledemo.R;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class TextReceiver extends BroadcastReceiver {
    String packageName;
    String permissionName;
    private NotificationManager Manager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("SEND_TO_SERVICE")){
            Bundle bundle = intent.getBundleExtra("Bundle");

            packageName = bundle.getString("PKG_NAME");
            permissionName = bundle.getString("PERMISSION_NAME");

            Log.d("Mulin", "Service receive: "+packageName + permissionName);

            Manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelID = "1";
            String channelName = "MyChannel";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            Manager.createNotificationChannel(channel);


            Intent allowIntent = new Intent("ACTION");
            allowIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("Decision", "Allow")
                    .putExtra("Key", packageName+permissionName);
            PendingIntent allowPendingIntent =
                    PendingIntent.getBroadcast(context, 0, allowIntent, FLAG_CANCEL_CURRENT);

            Intent denyIntent = new Intent("ACTION");
            denyIntent.putExtra("Decision", "Deny");
            PendingIntent denyPendingIntent =
                    PendingIntent.getBroadcast(context, 1, denyIntent, 0);

            Intent fakeIntent = new Intent("ACTION");
            fakeIntent.putExtra("Decision", "Fake");
            PendingIntent fakePendingIntent =
                    PendingIntent.getBroadcast(context, 2, fakeIntent, 0);


            Notification.Builder builder=new Notification.Builder(context);
            builder.setContentTitle("Alert");
            builder.setSmallIcon(R.mipmap.logo)
                    .setContentText("app is trying to use "+permissionName)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo))
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setChannelId("1")
                    .addAction(R.mipmap.ic_launcher, "Allow",allowPendingIntent)
                    .addAction(R.mipmap.ic_launcher, "Deny",denyPendingIntent)
                    .addAction(R.mipmap.ic_launcher, "Fake",fakePendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText(packageName + "is trying to access " + permissionName));
            Manager.notify(1,builder.build());
            Log.d("Mulin", "notification no error");

        }
    }


}
