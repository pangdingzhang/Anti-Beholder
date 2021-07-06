package sg.edu.smu.xposedmoduledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AndroidAppHelper;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import sg.edu.smu.xposedmoduledemo.UI.HistoryPageFragment;
import sg.edu.smu.xposedmoduledemo.UI.SettingPageFragment;
import sg.edu.smu.xposedmoduledemo.services.MyService;
import sg.edu.smu.xposedmoduledemo.xposed.MyAppInfo;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView lv_app_list;
    public Handler mHandler = new Handler();
    public HashMap<String,Integer> mapping = new HashMap<String, Integer>();
    public List<List<String>> permissionList = new ArrayList<List<String>>();
    public List<MyAppInfo> appInfos;
    public ArrayAdapter<CharSequence> spinnerAdapter;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appInfos = ScanTool.scanLocalInstallAppList(MainActivity.this.getPackageManager());
        addPermissionList(appInfos);
        //using only one MainActivity
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        setDefaultFragment();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bv);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Intent startServiceIntent = new Intent(this, MyService.class);
        startService(startServiceIntent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Mulin", "MainActivity on destory");
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()){
                case R.id.item_setting:
                    transaction.replace(R.id.fragment_container, new SettingPageFragment(appInfos,permissionList));
                    transaction.commit();
                    return true;
                case R.id.item_history:
                    transaction.replace(R.id.fragment_container, new HistoryPageFragment(appInfos,permissionList));
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingPageFragment(appInfos,permissionList));
        transaction.commit();
    }


    private void addPermissionList(List<MyAppInfo> myAppInfos){
        SharedPreferences pref = this.getSharedPreferences("permission_info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        for (MyAppInfo myAppInfo : myAppInfos){
            String[] permissionArray = myAppInfo.getAppPermission();
            List<String> shortNameList = new ArrayList<>();
            for (String permission : permissionArray){
                String shortName = "No dangerous permission";
                if (!permission.equals("No dangerous permission")) {
                    shortName = permission.substring(19);
                    shortNameList.add(shortName);
                    //in the future, we should remove below two lines code, which are used to assign default values to sharedpreference
                    editor.putString(myAppInfo.getPackageName()+shortName,"2");
                    editor.apply();
                    continue;
                }
                shortNameList.add(shortName);
            }
            permissionList.add(shortNameList);
        }
    }
}
