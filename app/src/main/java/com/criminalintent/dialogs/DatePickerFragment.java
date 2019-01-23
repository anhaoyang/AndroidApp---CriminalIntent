package com.criminalintent.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import com.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATA=
            "com.criminalintent.date";

    private Date mDate;

    public static DatePickerFragment newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_DATA,date);

        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent i=new Intent();
        i.putExtra(EXTRA_DATA,mDate);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,i);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDate=(Date)getArguments().getSerializable(EXTRA_DATA);
        Calendar calendar=Calendar.getInstance();
        if(mDate!=null){calendar.setTime(mDate);}
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        View v=getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date,null);

        DatePicker datePicker=(DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate=new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                getArguments().putSerializable(EXTRA_DATA,mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
}
