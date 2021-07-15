package sg.edu.smu.xposedmoduledemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sg.edu.smu.xposedmoduledemo.UI.AppTimelineAdapter;
import sg.edu.smu.xposedmoduledemo.UI.TimelineAdapter;
import sg.edu.smu.xposedmoduledemo.UI.TimelineItemDecoration;

public class AppPermissionHistoryActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ImageView iv;
    private TextView tv;
    private AppTimelineAdapter appTimelineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_permission_history);
        String appName = getIntent().getStringExtra("APP_NAME");
        rv = findViewById(R.id.app_permission_history_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new TimelineItemDecoration());
        appTimelineAdapter = new AppTimelineAdapter(this, appName);
        rv.setAdapter(appTimelineAdapter);


        PackageManager pm = this.getPackageManager();
        List<ApplicationInfo> l = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String pkgName = "";
        for (ApplicationInfo ai : l){
            String n = (String)pm.getApplicationLabel(ai);
            if (n.equals(appName)){
                pkgName = ai.packageName; // retrieve package name here
            }
        }

        iv = findViewById(R.id.app_permission_history_app_logo);
        try {
            iv.setImageDrawable(this.getPackageManager().getApplicationIcon(pkgName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv= findViewById(R.id.app_permission_history_app_name);
        tv.setText(appName);
    }
}