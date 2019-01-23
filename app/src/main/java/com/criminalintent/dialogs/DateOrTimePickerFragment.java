package com.criminalintent.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.criminalintent.R;

public class DateOrTimePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE_OR_TIME_RESULT=
            "com.criminalintent.date_or_time_result";

    public static DateOrTimePickerFragment newInstance(){
        Bundle args=new Bundle();
        DateOrTimePickerFragment dateOrTimePickerFragment=new DateOrTimePickerFragment();
        dateOrTimePickerFragment.setArguments(args);
        return dateOrTimePickerFragment;
    }

    private void sendResult(int resultCode,String resultType){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE_OR_TIME_RESULT,resultType);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_date_or_time,null);

        Button dateButton=(Button)v.findViewById(R.id.dialog_date_or_time_buttonDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK,"date");
            }
        });
        Button timeButton=(Button)v.findViewById(R.id.dialog_date_or_time_buttonTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK,"time");
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_date_or_time_title)
                .create();
    }




}
