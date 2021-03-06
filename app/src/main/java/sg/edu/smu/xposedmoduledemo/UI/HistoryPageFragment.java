package sg.edu.smu.xposedmoduledemo.UI;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import sg.edu.smu.xposedmoduledemo.util.DBHelper;
import sg.edu.smu.xposedmoduledemo.R;
import sg.edu.smu.xposedmoduledemo.xposed.MyAppInfo;

public class HistoryPageFragment extends Fragment {
    private ExpandableListView expandableListView;
    private List<MyAppInfo> myAppInfos;
    private List<List<String>> permissions;
    private HistoryPageFragment.AppAdapter.ViewHolder mViewHolder;
    private HashMap<String,Integer> mapping = new HashMap<String, Integer>();
    private DBHelper dbHelper;

    public HistoryPageFragment(){
        super(R.layout.report_group_item);
    }

    public HistoryPageFragment(List<MyAppInfo> myAppInfos, List<List<String>> permissions){
        this.myAppInfos = myAppInfos;
        this.permissions = permissions;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapping.put("android.permission.READ_EXTERNAL_STORAGE", R.mipmap.file);
        mapping.put("android.permission.READ_CONTACTS", R.mipmap.contact);
        mapping.put("android.permission.ACCESS_FINE_LOCATION", R.mipmap.location);
        mapping.put("android.permission.ACCESS_COARSE_LOCATION", R.mipmap.location);
        mapping.put("android.permission.READ_PHONE_STATE", R.mipmap.phone);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        expandableListView = (ExpandableListView)view.findViewById(R.id.lv_app_list);
        expandableListView.setAdapter(new HistoryPageFragment.AppAdapter(getContext()));
        return view;

    }

    class AppAdapter extends BaseExpandableListAdapter {
        Context context;


        public AppAdapter(Context context){
            this.context=context;

        }

        private void addPermissionIcons(String[] permissions) {

            for (String permission : permissions){
                Integer logoId = mapping.get(permission);
                if (null != logoId){
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //??????????????????
                    imageView.setImageResource(logoId); //????????????
                    mViewHolder.tx_app_description.addView(imageView); //??????????????????
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


            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            AppAdapter.ChildViewHolder childViewHolder;
            dbHelper = new DBHelper(context, "Permission.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] tableColumns = new String[]{"time"};
            String whereClause = "package_name = ? AND permission = ?";
            String[] whereArgs = new String[]{myAppInfos.get(groupPosition).getPackageName(),permissions.get(groupPosition).get(childPosition)};
            Cursor cursor1 = db.query("permission_db", tableColumns, whereClause, whereArgs,null,null,null);
            String dateAsString = "N/A";
            if (cursor1.moveToFirst()){
                do {
                    Long time = cursor1.getLong(cursor1.getColumnIndex("time"));
                    dateAsString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date(time));
                } while (cursor1.moveToNext());
            }

            String whereClause2 = "package_name = ? AND permission = ? AND strftime('%s','now') * 1000 - time < 24*60*60*1000";
            // 5 mins * 60 seconds * 1000 mile seconds
            String[] whereArgs2 = new String[]{myAppInfos.get(groupPosition).getPackageName(),permissions.get(groupPosition).get(childPosition)};
            long count = DatabaseUtils.queryNumEntries(db, "permission_db",whereClause2,whereArgs2);

            if (convertView==null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.repory_sub_item,parent,false);
                childViewHolder = new AppAdapter.ChildViewHolder();
                childViewHolder.children_item = (TextView)convertView.findViewById(R.id.p_tv_app_name);
                childViewHolder.children_icon = (ImageView) convertView.findViewById(R.id.p_iv_app_icon);
                childViewHolder.children_detail = (TextView) convertView.findViewById(R.id.p_tv_app_data);
                convertView.setTag(childViewHolder);

            }else {
                childViewHolder = (AppAdapter.ChildViewHolder) convertView.getTag();
            }
            childViewHolder.children_item.setText(permissions.get(groupPosition).get(childPosition));
            Integer logoResource = mapping.get("android.permission."+permissions.get(groupPosition).get(childPosition));
            if (null != logoResource){
                childViewHolder.children_icon.setImageResource(logoResource);
            }
//            childViewHolder.children_icon.setImageResource(mapping.get(permissions.get(groupPosition).get(childPosition)));
            childViewHolder.children_detail.setText("Total access times: "+ count +"\nLast Access Time: "+dateAsString);

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
            ImageView children_icon;
            TextView children_detail;
        }
    }
}
