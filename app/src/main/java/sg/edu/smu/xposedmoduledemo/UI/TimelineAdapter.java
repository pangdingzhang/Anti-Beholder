package sg.edu.smu.xposedmoduledemo.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import sg.edu.smu.xposedmoduledemo.util.DBHelper;
import sg.edu.smu.xposedmoduledemo.R;

public class TimelineAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private DBHelper dbHelper;
    private Cursor cursor;
    private int entries;
    private String permissionFilter;

    public TimelineAdapter(){}
    public TimelineAdapter(Context context, String permissionFilter){
        this.context = context;
        this.permissionFilter = permissionFilter;
        this.inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context, "Permission.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] tableColumns = new String[]{"package_name","permission","time"};
        if(permissionFilter.equals("All Permission")){
            // query the latest 5 minutes. (60 mins * 60 seconds * 1000 mile seconds)
            String whereClause = "strftime('%s','now') * 1000 - time < 24*60*60*1000";
            cursor = db.query("permission_db", tableColumns, whereClause, null,null,null,null);
        } else{
            String whereClause = "permission = ? AND strftime('%s','now') * 1000 - time < 24*60*60*1000";
            String[] whereArgs = new String[]{permissionFilter};
            cursor = db.query("permission_db", tableColumns, whereClause, whereArgs,null,null,null);
        }
        entries = cursor.getCount();
        cursor.moveToFirst();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.report_timeline, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        cursor.moveToPosition(position);
        if(cursor.getCount() <= 0){
            Log.d("Mulin", "cursor error");
        }
        String packageName = cursor.getString(cursor.getColumnIndex("package_name"));
        String permission = cursor.getString(cursor.getColumnIndex("permission"));
        Long time = cursor.getLong(cursor.getColumnIndex("time"));
        String dateAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(time));
        try{
            vh.name.setText(context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(packageName, 0)));
            vh.logo.setImageDrawable(context.getPackageManager().getApplicationIcon(packageName));
            vh.description.setText(dateAsString+" "+permission);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        vh.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = (TextView) view;

                Intent intent = new Intent(view.getContext(), AppPermissionHistoryActivity.class);
                intent.putExtra("APP_NAME", name.getText());
                view.getContext().startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return (int) entries;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView logo;
        private TextView name, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.tl_iv_icon);
            name = itemView.findViewById(R.id.tl_tv_name);
            description = itemView.findViewById(R.id.tl_tv_description);

        }

        public ImageView getLogo() {
            return logo;
        }

        public TextView getName() {
            return name;
        }

        public TextView getDescription() {
            return description;
        }
    }


}
