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
    int buttonId;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("Click_Event")){
            Bundle bundle = intent.getBundleExtra("Bundle");

            text = bundle.getString("BUTTON_TEXT");
            buttonId = bundle.getInt("BUTTON_ID");

            showAToast(context,text+buttonId);

        }
    }

    public void showAToast (Context context, String message){
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public int getButtonId() {
        return buttonId;
    }

    public String getText() {
        return text;
    }
}
