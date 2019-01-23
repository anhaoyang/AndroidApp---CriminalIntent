package com.criminalintent.activitys.crime_page;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.criminalintent.activitys.crime_camera_page.CrimeCameraActivity;
import com.criminalintent.activitys.crime_camera_page.CrimeCameraFragment;
import com.criminalintent.bean.Photo;
import com.criminalintent.dialogs.DateOrTimePickerFragment;
import com.criminalintent.dialogs.DatePickerFragment;
import com.criminalintent.R;
import com.criminalintent.dialogs.ImageFragment;
import com.criminalintent.dialogs.TimePickerFragment;
import com.criminalintent.bean.Crime;
import com.criminalintent.lab.CrimeLab;
import com.criminalintent.utils.PictrueUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String TAG="CrimeFragment";
    public static final String EXTRA_CRIME_ID=
            "com.criminalintent.crime_id";

    private Crime mCrime;
    private ImageButton mPhotoButton;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageView mPhotoView;

    private static final String DIALOG_DATE_OR_TIME="date_or_time";
    private static final String DIALOG_DATE="date";
    private static final String DIALOG_TIME="time";
    private static final String DIALOG_IMAGE="image";
    private static final int REQUEST_DATE_OR_TIME=0;
    private static final int REQUEST_DATE=1;
    private static final int REQUEST_TIME=2;
    private static final int REQUEST_PHOTO=3;
    private DateOrTimePickerFragment dateOrTimePickerFragmen;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeId);

        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime= CrimeLab.get(getActivity()).getCrime(crimeId);
        //防止多次赋值
        if(getString(R.string.app_name).equals(getActivity().getTitle())){
            getActivity().setTitle(mCrime.getTitle());
            //Log.d("设置标题",mCrime.getTitle());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_details,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_delete_crime_details:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        //启用应用图标向上导航 该方法来自 API 11，所以需要做判断
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            //如果有父Activity，就显示向左图标
            if(NavUtils.getParentActivityName(getActivity())!=null){
//                Log.d("activity",String.valueOf(getActivity()==null));
//                Log.d("Action",String.valueOf(((AppCompatActivity)getActivity()).getSupportActionBar()==null));
                //注意：该方法只是让应用图标变成按钮，并显示一个向左的图标
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mPhotoView=v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p=mCrime.getPhoto();
                FragmentManager fm=getActivity().getSupportFragmentManager();
                if(p==null){
                    return;
                }

                String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm,DIALOG_IMAGE);
            }
        });
        registerForContextMenu(mPhotoView);

        mPhotoButton=v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i,REQUEST_PHOTO);
            }
        });
        //检查设备有没有相机
        PackageManager pm=getActivity().getPackageManager();
        boolean hasCamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                ||pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                ||Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                || Camera.getNumberOfCameras()>0;
        if(!hasCamera){
            mPhotoButton.setEnabled(false);
        }

        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mDateButton=(Button)v.findViewById(R.id.crime_date);
        updateDate();;
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                dateOrTimePickerFragmen=DateOrTimePickerFragment.newInstance();
                dateOrTimePickerFragmen.setTargetFragment(CrimeFragment.this,REQUEST_DATE_OR_TIME);
                dateOrTimePickerFragmen.show(fm,DIALOG_DATE_OR_TIME);
//                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getDate());
//                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
//                dialog.show(fm,DIALOG_DATE);
            }
        });

        mSolvedCheckBox=(CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.fragment_crime_details_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fragment_crime_details_context_menu_delete_photo:
                CrimeLab.get(getActivity()).deletePhoto(mCrime);
                refreshPhoto();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showPhoto(){
        Photo p=mCrime.getPhoto();
        BitmapDrawable b=null;
        if(p!=null){
            String path=getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b= PictrueUtils.getScaledDrawable(getActivity(),path);
        }
        mPhotoView.setImageDrawable(b);
    }

    private void refreshPhoto(){
        PictrueUtils.clearImageView(mPhotoView);
        showPhoto();
    }


    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictrueUtils.clearImageView(mPhotoView);
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=Activity.RESULT_OK){return;}
        if(requestCode==REQUEST_DATE_OR_TIME){ //选择了修改的是日期还是时间
            String resultType=(String)data.getStringExtra(DateOrTimePickerFragment.EXTRA_DATE_OR_TIME_RESULT);
            FragmentManager fm=getActivity().getSupportFragmentManager();
            if("date".equals(resultType)){
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
            }else if("time".equals(resultType)) {
                TimePickerFragment dialog=TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                dialog.show(fm,DIALOG_TIME);
            }
            dateOrTimePickerFragmen.dismiss();
        }else if(requestCode==REQUEST_DATE){ //修改了日期
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATA);
            Calendar dateC=Calendar.getInstance();
            if(date!=null){
                dateC.setTime(date);
            }
            //只更改日期
            Calendar calendar=Calendar.getInstance();
            if(mCrime.getDate()!=null){
                calendar.setTime(mCrime.getDate());
            }
            calendar.set(Calendar.YEAR,dateC.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH,dateC.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH,dateC.get(Calendar.DAY_OF_MONTH));
            mCrime.setDate(calendar.getTime());
            //Log.d("更新日期",calendar.toString());
            updateDate();
        }else if(requestCode==REQUEST_TIME){ //修改了时间
            Date time=(Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            Calendar timeC=Calendar.getInstance();
            if(time!=null){
                timeC.setTime(time);
            }
            //只更改时间
            Calendar calendar=Calendar.getInstance();
            if(mCrime.getDate()!=null){
                calendar.setTime(mCrime.getDate());
            }
            calendar.set(Calendar.HOUR_OF_DAY,timeC.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,timeC.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,timeC.get(Calendar.SECOND));
            mCrime.setDate(calendar.getTime());
            //Log.d("更新时间",calendar.toString());
            updateDate();
        }else if(requestCode==REQUEST_PHOTO){ // 拍照返回
            String filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename!=null){
                //Log.i(TAG,"filename: "+filename);
                Photo photo=new Photo(filename);
                if(mCrime.getPhoto()!=null){
                    CrimeLab.get(getActivity()).deletePhoto(mCrime);
                }
                mCrime.setPhoto(photo);
                refreshPhoto();
                Log.i(TAG,"Crime: "+mCrime.getTitle()+" has a photo");
            }
        }
    }

    private void updateDate() {
        if(mCrime.getDate()!=null){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            mDateButton.setText(sdf.format(mCrime.getDate()));
        }else{
            mDateButton.setText(R.string.date_picker_title_nodate);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存crimes到文件
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
