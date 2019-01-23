package com.criminalintent.activitys.crime_list;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.criminalintent.R;
import com.criminalintent.activitys.crime_page.CrimeFragment;
import com.criminalintent.activitys.crime_page.CrimePagerActivity;
import com.criminalintent.bean.Crime;
import com.criminalintent.lab.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private static final int REQUEST_CRIME=1;
    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //告诉fragmentManager应接收onCreateOptionsMenu方法的调用指令
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.crimes_title);
        mCrimes= CrimeLab.get(getActivity()).getCrimes();

        ArrayAdapter<Crime> adapter= new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
        //设置保留Fragment的属性值
        setRetainInstance(true);
        mSubtitleVisible=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.list_crimes,null);
        View emptyView=(View)v.findViewById(android.R.id.empty);
        Button addCrime=(Button)emptyView.findViewById(R.id.list_crimes_empty_view_add_crime);
        addCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("你你你你","aaaa");
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i=new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                startActivityForResult(i,0);
            }
        });

        ListView listView=(ListView)v.findViewById(android.R.id.list);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
            //为上下文菜单登记ListView
            registerForContextMenu(listView);
        }else{
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //设置监听器
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    //当条目的选择状态发生改变
                }

                @Override //ActionMode 的回调方法
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            //如果是删除就全部删除
                            CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab=CrimeLab.get(getActivity());
                            for (int i=adapter.getCount()-1;i>=0;i--){
                                //挨个查看是不是被选中
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        //在设备旋转后，创建视图时，设置子标题
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if(mSubtitleVisible){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem item=menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible&&item!=null){
            item.setTitle(R.string.hide_subtitle);
            item.setIcon(android.R.drawable.arrow_up_float);
        }
    }

    //阻止Android Lint报告兼容性问题
//    @TargetApi(11)
    //响应菜单选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i=new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if(((AppCompatActivity)getActivity()).getSupportActionBar().getSubtitle()==null){
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    item.setIcon(android.R.drawable.arrow_up_float);
                    mSubtitleVisible=true;
                }else{
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    item.setIcon(android.R.drawable.arrow_down_float);
                    mSubtitleVisible=false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //选择该activity的上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    //响应上下文菜单选项
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取选择的是哪个，即长按的哪个要求删除
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position=info.position;
        CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
        Crime crime=adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c=((CrimeAdapter)getListAdapter()).getItem(position);
        Intent i=new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());
        startActivityForResult(i,REQUEST_CRIME);
    }

    //当子acivity有了结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CRIME){
            // 处理结果
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(),0,crimes);
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            if(convertView==null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }
            Crime c=getItem(position);
            TextView titleTextView=
                    (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView=
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            if(c.getDate()!=null){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                dateTextView.setText(sdf.format(c.getDate()));
            }else{
                dateTextView.setText(R.string.date_picker_title_nodate);
            }
            //dateTextView.setText(String.valueOf(c.getDate()));
            CheckBox solvedCheckBox=
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());
            return convertView;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存crimes到文件
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
