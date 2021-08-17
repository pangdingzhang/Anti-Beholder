package sg.edu.smu.xposedmoduledemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table permission_db(id INTEGER PRIMARY KEY AUTOINCREMENT, package_name TEXT,app_name TEXT, permission TEXT,time INTEGER)";
        sqLiteDatabase.execSQL(sql);
        Toast.makeText(mContext, "Create Successfully", Toast.LENGTH_SHORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            sqLiteDatabase.execSQL("drop table if exists permission_db");
            onCreate(sqLiteDatabase);
        }
    }
}
