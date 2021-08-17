package sg.edu.smu.xposedmoduledemo.UI;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import sg.edu.smu.xposedmoduledemo.util.DBHelper;
import sg.edu.smu.xposedmoduledemo.R;

public class AppTimelineAdapter extends RecyclerView.Adapter{

    private Context context;
    private LayoutInflater inflater;
    private DBHelper dbHelper;
    private Cursor cursor;
    private int entries;
    private String appFilter;

    public AppTimelineAdapter(){}
    public AppTimelineAdapter(Context context, String appFilter){
        this.context = context;
        this.appFilter = appFilter;
        this.inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context, "Permission.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] tableColumns = new String[]{"permission","time"};
        String whereClause = "app_name = ? AND strftime('%s','now') * 1000 - time < 24*60*60*1000";
        String[] whereArgs = new String[]{appFilter};
        cursor = db.query("permission_db", tableColumns, whereClause, whereArgs,null,null,null);
        entries = cursor.getCount();
        cursor.moveToFirst();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppTimelineAdapter.ViewHolder(inflater.inflate(R.layout.app_level_report_timeline, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppTimelineAdapter.ViewHolder vh = (AppTimelineAdapter.ViewHolder) holder;
        cursor.moveToPosition(position);
        if(cursor.getCount() <= 0){
            Log.d("Mulin", "cursor error");
        }
        String permission = cursor.getString(cursor.getColumnIndex("permission"));
        Long time = cursor.getLong(cursor.getColumnIndex("time"));
        String dateAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(time));
        vh.name.setText(permission);
        vh.description.setText(dateAsString);

    }

    @Override
    public int getItemCount() {
        return entries;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.app_level_tl_tv_name);
            description = itemView.findViewById(R.id.app_level_tl_tv_description);

        }

        public TextView getName() {
            return name;
        }

        public TextView getDescription() {
            return description;
        }
    }
}
