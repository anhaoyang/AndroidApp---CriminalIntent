package com.criminalintent.activitys.crime_camera_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.criminalintent.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CrimeCameraFragment extends Fragment {
    private static final String TAG="CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME=
            "com.criminalintent.activitys.crime_camera_page.photo_name";

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    //定义在点击拍照按钮时操作
    private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };
    //定义在Camera已经生成jpeg图像时操作
    private Camera.PictureCallback mPictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //保存图片
            String filename= UUID.randomUUID().toString()+".jpg";
            FileOutputStream os=null;
            boolean success=true;
            try {
                os=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e) {
                Log.e(TAG,"Error writing to file "+filename,e);
                success=false;
            } finally {
                try {
                    if(os!=null){
                        os.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Error closing file "+filename,e);
                    success=false;
                }
            }

            if(success){
                //Log.i(TAG,"JPEG saved at "+filename);
                Intent i=new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,filename);
                getActivity().setResult(Activity.RESULT_OK,i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime_camera,container,false);

        mProgressContainer=v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        Button takePictureButton=(Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                if(mCamera!=null) {
                    mCamera.takePicture(mShutterCallback, null, mPictureCallback);
                }
            }
        });

        mSurfaceView=v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder=mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(mCamera!=null){
                        //将 Camera 绑定到 此 SurfaceHolder 上
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Error setting up preview display",e);
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera==null){
                    return ;
                }
                Camera.Parameters parameters=mCamera.getParameters();
                //设置预览的界面大小
                Camera.Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(s.width,s.height);
                //设置相机生成的图片的大小
                s=getBestSupportedSize(parameters.getSupportedPictureSizes(),s.width,s.height);
                parameters.setPictureSize(s.width,s.height);
                //将设置参数给相机
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG,"Could not start preview",e);
                    mCamera.release();
                    mCamera=null;
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null){
                    mCamera.stopPreview();
                }
            }
        });
        return v;
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize=sizes.get(0);
        int largestArea=bestSize.width*bestSize.height;
        for(Camera.Size s:sizes){
            int area=s.width*s.height;
            if(area>largestArea){
                bestSize=s;
                largestArea=area;
            }
        }
        return bestSize;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
                mCamera=Camera.open(0);
            }else{
                mCamera=Camera.open();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(),"请打开相机权限",Toast.LENGTH_LONG).show();
            //getActivity().finish();
        }
//        if(mCamera==null){
//            Toast.makeText(getActivity(),"打开相机失败",Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }


}
