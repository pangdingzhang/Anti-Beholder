package sg.edu.smu.xposedmoduledemo.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ButtonSingleton {
    private static ButtonSingleton instance = null;
    private int id;
    private String text;
    private Toast toast;

    public synchronized static ButtonSingleton getInstance() {
        if (instance == null) {
            instance = new ButtonSingleton();
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(int id, String text){
        this.id = id;
        this.text = text;
        Log.d("Mulin", "singleton execute once");
        showAToast(AndroidAppHelper.currentApplication(),text+id);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void showAToast (Context context, String message){
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
