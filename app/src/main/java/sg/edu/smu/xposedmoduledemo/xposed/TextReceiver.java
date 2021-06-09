package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class TextReceiver extends BroadcastReceiver {
    String text;
    Toast toast;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("Click_Event")){
            Bundle bundle = intent.getBundleExtra("Bundle");
            if (!bundle.getString("BUTTON_TEXT").equals(text)) {
                text = bundle.getString("BUTTON_TEXT");
            }
            showAToast(context,text);

        }
    }

    public void showAToast (Context context, String message){
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
