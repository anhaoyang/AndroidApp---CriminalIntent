package com.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import java.io.IOException;

public class PictrueUtils {
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a, String path){
        Display display=a.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if(srcHeight>destHeight || srcWidth>destWidth){
            if(srcWidth>srcHeight){
                inSampleSize=Math.round(srcHeight/destHeight);
            }else{
                inSampleSize=Math.round(srcWidth/destWidth);
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        Bitmap bm=BitmapFactory.decodeFile(path,options);

        //正确旋转照片
        int digree = 0;
        ExifInterface exifInterface  = null;
        try {
            exifInterface  = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            exifInterface  = null;
        }
        if (exifInterface  != null) {
            // 读取图片中相机方向信息
            int ori = exifInterface .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        if (digree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(digree);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), m, true);
        }
//        Log.d("图片旋转",String.valueOf(exifInterface  != null)+"图片旋转："+digree);
//        String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//        String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//        String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
//        String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
//        String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
//        String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//        String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
//        String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//        String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//        String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//        String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//        String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
//        String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
//        String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO);
//        String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
//        String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
//        String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG);
//        String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG);
//        String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
//        String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
//        String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
//        String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
//        String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
//        String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
//        String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
//
//        Log.e("TAG", "## orientation=" + orientation);
//        Log.e("TAG", "## dateTime=" + dateTime);
//        Log.e("TAG", "## make=" + make);
//        Log.e("TAG", "## model=" + model);
//        Log.e("TAG", "## flash=" + flash);
//        Log.e("TAG", "## imageLength=" + imageLength);
//        Log.e("TAG", "## imageWidth=" + imageWidth);
//        Log.e("TAG", "## latitude=" + latitude);
//        Log.e("TAG", "## longitude=" + longitude);
//        Log.e("TAG", "## latitudeRef=" + latitudeRef);
//        Log.e("TAG", "## longitudeRef=" + longitudeRef);
//        Log.e("TAG", "## exposureTime=" + exposureTime);
//        Log.e("TAG", "## aperture=" + aperture);
//        Log.e("TAG", "## isoSpeedRatings=" + isoSpeedRatings);
//        Log.e("TAG", "## dateTimeDigitized=" + dateTimeDigitized);
//        Log.e("TAG", "## subSecTime=" + subSecTime);
//        Log.e("TAG", "## subSecTimeOrig=" + subSecTimeOrig);
//        Log.e("TAG", "## subSecTimeDig=" + subSecTimeDig);
//        Log.e("TAG", "## altitude=" + altitude);
//        Log.e("TAG", "## altitudeRef=" + altitudeRef);
//        Log.e("TAG", "## gpsTimeStamp=" + gpsTimeStamp);
//        Log.e("TAG", "## gpsDateStamp=" + gpsDateStamp);
//        Log.e("TAG", "## whiteBalance=" + whiteBalance);
//        Log.e("TAG", "## focalLength=" + focalLength);
//        Log.e("TAG", "## processingMethod=" + processingMethod);
        return new BitmapDrawable(a.getResources(),bm);
    }

    public static void clearImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable)){
            return ;
        }
        BitmapDrawable b=(BitmapDrawable)imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
