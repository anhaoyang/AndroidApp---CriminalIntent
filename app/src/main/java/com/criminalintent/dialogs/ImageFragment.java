package com.criminalintent.dialogs;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.criminalintent.utils.PictrueUtils;

public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH=
            "com.criminalintent.dialogs.image_path";
    public static ImageFragment newInstance(String imagePath){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH,imagePath);

        ImageFragment imageFragment=new ImageFragment();
        imageFragment.setArguments(args);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return imageFragment;
    }

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mImageView=new ImageView(getActivity());
        String path=(String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image= PictrueUtils.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);

        return mImageView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PictrueUtils.clearImageView(mImageView);
    }
}
