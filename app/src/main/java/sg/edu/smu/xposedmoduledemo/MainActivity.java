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

        //setting page is an activity
//        lv_app_list = (ExpandableListView) findViewById(R.id.lv_app_list);
//        Display newDisplay = getWindowManager().getDefaultDisplay();
//        int width = newDisplay.getWidth();
//        lv_app_list.setIndicatorBounds(width-100, width);
//        initAppList();
//        mapping.put("android.permission.READ_EXTERNAL_STORAGE", R.mipmap.file);
//        mapping.put("android.permission.READ_CONTACTS", R.mipmap.contact);
//        mapping.put("android.permission.ACCESS_FINE_LOCATION", R.mipmap.location);
//        pref= getSharedPreferences("permission_info",Context.MODE_MULTI_PROCESS);
//        editor = pref.edit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Mulin", "MainActivity on destory");
    }
//    private void initAppList() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                //Scan all apps
//                final List<MyAppInfo> appInfos = ScanTool.scanLocalInstallAppList(MainActivity.this.getPackageManager());
//                addPermissionList(appInfos);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAppAdapter = new AppAdapter(MainActivity.this,appInfos,permissionList);
//                        spinnerAdapter = ArrayAdapter.createFromResource(MainActivity.this,
//                                R.array.option_array, android.R.layout.simple_spinner_item);
//                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        lv_app_list.setAdapter(mAppAdapter);
//
//                    }
//                });
//            }
//        }.start();
//    }

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

//    class AppAdapter extends BaseExpandableListAdapter {
//
//        List<MyAppInfo> myAppInfos;
//        List<List<String>> permissions;
//        Context context;
//
//
//        public AppAdapter(Context context,List<MyAppInfo> myAppInfos, List<List<String>> permissions){
//            this.context=context;
//            this.myAppInfos=myAppInfos;
//            this.permissions=permissions;
//        }
//
////        public void setData(List<MyAppInfo> myAppInfos) {
////            this.myAppInfos = myAppInfos;
////            notifyDataSetChanged();
////        }
////
////        public List<MyAppInfo> getData() {
////            return myAppInfos;
////        }
////
////        @Override
////        public int getCount() {
////            if (myAppInfos != null && myAppInfos.size() > 0) {
////                return myAppInfos.size();
////            }
////            return 0;
////        }
////
////        @Override
////        public Object getItem(int position) {
////            if (myAppInfos != null && myAppInfos.size() > 0) {
////                return myAppInfos.get(position);
////            }
////            return null;
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return 0;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////
////            MyAppInfo myAppInfo = myAppInfos.get(position);
////            if (convertView == null) {
////                mViewHolder = new ViewHolder();
////                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
////                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
////                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
////                mViewHolder.tx_app_description = (LinearLayout) convertView.findViewById(R.id.tv_icons);
////                convertView.setTag(mViewHolder);
////            } else {
////                mViewHolder = (ViewHolder) convertView.getTag();
////            }
////
////            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
////            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
////            mViewHolder.tx_app_description.removeAllViews();
////            addPermissionIcons(myAppInfo.getAppPermission());
////            return convertView;
////        }
//
//
//        private void addPermissionIcons(String[] permissions) {
//
//            for (String permission : permissions){
//                Integer logoId = mapping.get(permission);
//                if (null != logoId){
//                    ImageView imageView = new ImageView(MainActivity.this);
//                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //设置图片宽高
//                    imageView.setImageResource(logoId); //图片资源
//                    mViewHolder.tx_app_description.addView(imageView); //动态添加图片
//                }
//            }
//            Log.d("Mulin", "Nothing found");
//        }
//
//        @Override
//        public int getGroupCount() {
//            if (myAppInfos != null && myAppInfos.size() > 0) {
//                return myAppInfos.size();
//            }
//            return 0;
//        }
//
//        @Override
//        public int getChildrenCount(int i) {
//            return permissions.get(i).size();
//        }
//
//        @Override
//        public Object getGroup(int i) {
//            if (myAppInfos != null && myAppInfos.size() > 0) {
//                return myAppInfos.get(i);
//            }
//            return null;
//        }
//
//        @Override
//        public Object getChild(int i, int i1) {
//            return permissions.get(i).get(i1);
//        }
//
//        @Override
//        public long getGroupId(int i) {
//            return i;
//        }
//
//        @Override
//        public long getChildId(int i, int i1) {
//            return i1;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        @Override
//        public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
//            MyAppInfo myAppInfo = myAppInfos.get(position);
//            if (convertView == null) {
//                mViewHolder = new ViewHolder();
//                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
//                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
//                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
//                mViewHolder.tx_app_description = (LinearLayout) convertView.findViewById(R.id.tv_icons);
//                convertView.setTag(mViewHolder);
//            } else {
//                mViewHolder = (ViewHolder) convertView.getTag();
//            }
//
//            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
//            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
//            mViewHolder.tx_app_description.removeAllViews();
//            addPermissionIcons(myAppInfo.getAppPermission());
//
//
//            return convertView;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//            ChildViewHolder childViewHolder;
//            if (convertView==null){
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_permission,parent,false);
//                childViewHolder = new ChildViewHolder();
//                childViewHolder.children_item = (TextView)convertView.findViewById(R.id.chidren_item);
//                childViewHolder.children_spinner = convertView.findViewById(R.id.options_spinner);
//                convertView.setTag(childViewHolder);
//
//            }else {
//                childViewHolder = (ChildViewHolder) convertView.getTag();
//            }
//            childViewHolder.children_item.setText(permissions.get(groupPosition).get(childPosition));
//            childViewHolder.children_spinner.setAdapter(spinnerAdapter);
//            childViewHolder.children_spinner.setSelection(Integer.parseInt(
//                    pref.getString(myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition),"1")));
//            childViewHolder.children_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    Log.d("Mulin", "MainActivity: "+myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition));
//                    editor.putString(myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition),Integer.toString(i));
//                    editor.apply();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//            return convertView;
//        }
//
//        @Override
//        public boolean isChildSelectable(int i, int i1) {
//            return false;
//        }
//
//        class ViewHolder {
//            ImageView iv_app_icon;
//            TextView tx_app_name;
//            LinearLayout tx_app_description;
//        }
//
//        class ChildViewHolder{
//            TextView children_item;
//            Spinner children_spinner;
//        }
//    }

    private void addPermissionList(List<MyAppInfo> myAppInfos){
        for (MyAppInfo myAppInfo : myAppInfos){
            String[] permissionArray = myAppInfo.getAppPermission();
            List<String> shortNameList = new ArrayList<>();
            for (String permission : permissionArray){
                String shortName = "No dangerous permission";
                if (!permission.equals("No dangerous permission")) {
                    shortName = permission.substring(19);
                    shortNameList.add(shortName);
                    continue;
                }
                shortNameList.add(shortName);
            }
            permissionList.add(shortNameList);
        }
    }
}
