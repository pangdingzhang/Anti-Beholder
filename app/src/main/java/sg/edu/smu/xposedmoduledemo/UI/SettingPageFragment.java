package sg.edu.smu.xposedmoduledemo.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import sg.edu.smu.xposedmoduledemo.R;
import sg.edu.smu.xposedmoduledemo.xposed.MyAppInfo;

public class SettingPageFragment extends Fragment {
    private ExpandableListView expandableListView;
    private List<MyAppInfo> myAppInfos;
    private List<List<String>> permissions;
    private AppAdapter.ViewHolder mViewHolder;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefButton;
    private SharedPreferences.Editor editorButton;
    private HashMap<String,Integer> mapping = new HashMap<String, Integer>();
    private boolean buttonHookMode;


    public SettingPageFragment(){
        super(R.layout.item_app_info);
    }

    public SettingPageFragment(List<MyAppInfo> myAppInfos, List<List<String>> permissions, boolean buttonHookMode){
        this.myAppInfos = myAppInfos;
        this.permissions = permissions;
        this.buttonHookMode = buttonHookMode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapping.put("android.permission.READ_EXTERNAL_STORAGE", R.mipmap.file);
        mapping.put("android.permission.READ_CONTACTS", R.mipmap.contact);
        mapping.put("android.permission.ACCESS_FINE_LOCATION", R.mipmap.location);
        pref= getActivity().getSharedPreferences("permission_info",Context.MODE_MULTI_PROCESS);
        editor = pref.edit();
        prefButton= getActivity().getSharedPreferences("button_permission_info",Context.MODE_MULTI_PROCESS);
        editorButton = prefButton.edit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        expandableListView = (ExpandableListView)view.findViewById(R.id.lv_app_list);
        expandableListView.setAdapter(new AppAdapter(getContext()));
        return view;

    }

    class AppAdapter extends BaseExpandableListAdapter {
        Context context;
        public List<List<String>> buttonList = new ArrayList<>();
        public Set<String> buttonSet;


        public AppAdapter(Context context){
            this.context=context;

        }

        private void loadAllButtonPermissions(String pkgName){
            ArrayList permissionAndButtonId = new ArrayList();

            buttonSet = prefButton.getAll().keySet();
            for(String s : buttonSet){
                if (s.startsWith(pkgName)){
                    permissionAndButtonId.add(s.replace(pkgName, ""));
                }
            }
            buttonList.add(permissionAndButtonId);

        }

        private void addPermissionIcons(String[] permissions) {

            for (String permission : permissions){
                Integer logoId = mapping.get(permission);
                if (null != logoId){
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //设置图片宽高
                    imageView.setImageResource(logoId); //图片资源
                    mViewHolder.tx_app_description.addView(imageView); //动态添加图片
                }
            }
        }

        @Override
        public int getGroupCount() {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.size();
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int i) {
            if (buttonHookMode){
                return buttonList.get(i).size();
            }
            return permissions.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.get(i);
            }
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            if (buttonHookMode){
                return buttonList.get(i).get(i1);
            }
            return permissions.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {


            MyAppInfo myAppInfo = myAppInfos.get(position);
            if (convertView == null) {
                mViewHolder = new AppAdapter.ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                mViewHolder.tx_app_description = (LinearLayout) convertView.findViewById(R.id.tv_icons);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (AppAdapter.ViewHolder) convertView.getTag();
            }

            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
            mViewHolder.tx_app_description.removeAllViews();
            addPermissionIcons(myAppInfo.getAppPermission());
            loadAllButtonPermissions(myAppInfo.getPackageName());



            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            AppAdapter.ChildViewHolder childViewHolder;
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.option_array, android.R.layout.simple_spinner_item);;
            if (convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_permission,parent,false);
                childViewHolder = new AppAdapter.ChildViewHolder();
                childViewHolder.children_item = (TextView)convertView.findViewById(R.id.chidren_item);
                childViewHolder.children_spinner = convertView.findViewById(R.id.options_spinner);
                convertView.setTag(childViewHolder);

            }else {
                childViewHolder = (AppAdapter.ChildViewHolder) convertView.getTag();
            }
            childViewHolder.children_spinner.setAdapter(spinnerAdapter);
            if (buttonHookMode){
                childViewHolder.children_item.setText(buttonList.get(groupPosition).get(childPosition));
                childViewHolder.children_spinner.setSelection(Integer.parseInt(
                        prefButton.getString(myAppInfos.get(groupPosition).getPackageName()+buttonList.get(groupPosition).get(childPosition),"1")));
            }else{
                childViewHolder.children_item.setText(permissions.get(groupPosition).get(childPosition));
                childViewHolder.children_spinner.setSelection(Integer.parseInt(
                        pref.getString(myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition),"1")));
            }

            childViewHolder.children_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (buttonHookMode){
                        editorButton.putString(myAppInfos.get(groupPosition).getPackageName()+buttonList.get(groupPosition).get(childPosition),Integer.toString(i));
                        Log.d("Mulin", "prefix is "+myAppInfos.get(groupPosition).getPackageName()+buttonList.get(groupPosition).get(childPosition));
                        editorButton.apply();
                    } else {
                        Log.d("Mulin", "MainActivity: "+myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition));
                        editor.putString(myAppInfos.get(groupPosition).getPackageName()+permissions.get(groupPosition).get(childPosition),Integer.toString(i));
                        editor.apply();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
            LinearLayout tx_app_description;
        }

        class ChildViewHolder{
            TextView children_item;
            Spinner children_spinner;
        }
    }
}
