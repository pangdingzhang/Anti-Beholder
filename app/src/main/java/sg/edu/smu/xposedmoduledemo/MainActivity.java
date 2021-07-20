package sg.edu.smu.xposedmoduledemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sg.edu.smu.xposedmoduledemo.UI.HistoryPageFragment;
import sg.edu.smu.xposedmoduledemo.UI.SettingPageFragment;
import sg.edu.smu.xposedmoduledemo.UI.TimelineFragment;
import sg.edu.smu.xposedmoduledemo.pojos.AppPermissionBean;
import sg.edu.smu.xposedmoduledemo.services.MyService;
import sg.edu.smu.xposedmoduledemo.xposed.MyAppInfo;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView lv_app_list;
    public Handler mHandler = new Handler();
    public HashMap<String,Integer> mapping = new HashMap<String, Integer>();
    public List<List<String>> permissionList = new ArrayList<List<String>>();
    public List<MyAppInfo> appInfos;
    public Set<String> installedPackageNames = new HashSet<>();

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("permission_info",Context.MODE_PRIVATE);
        editor = pref.edit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_summary_report:
                Toast.makeText(this,"Add button clicked",Toast.LENGTH_SHORT).show();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new HistoryPageFragment(appInfos,permissionList));
                transaction.commit();
                break;
            case R.id.item_import_config_file:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");//无类型限制
                startActivityForResult(intent, 2);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            Log.d("Mulin", "path is "+path);
            String trimedPath = path.split(":")[1];
            Log.d("Mulin", "trimmed path is "+trimedPath);

            InputStreamReader inputStreamReader = null;
            try {
                File file = new File(trimedPath);
                FileInputStream in=new FileInputStream (file);
                inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();
                Log.d("Mulin", "sb is " + stringBuilder.toString());
                JsonObject jsonObject = new JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
                Log.d("Mulin", "hook_level=" + jsonObject.get("hook_level").getAsString());
                JsonArray jsonArray = jsonObject.getAsJsonArray("hooks");
                for (JsonElement hook : jsonArray) {

                    AppPermissionBean appPermissionBean = new Gson().fromJson(hook, new TypeToken<AppPermissionBean>() {}.getType());

                    //根据条件过滤
                    if (installedPackageNames.contains(appPermissionBean.getPackage_name())) {
                        editor.putString(appPermissionBean.getPackage_name()+appPermissionBean.getPermission(),appPermissionBean.getDecision());
                        editor.apply();
                    }
                }
                Toast.makeText(this, "Config File is imported successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    transaction.replace(R.id.fragment_container, new TimelineFragment());
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
        for (MyAppInfo myAppInfo : myAppInfos){
            installedPackageNames.add(myAppInfo.getPackageName());
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
