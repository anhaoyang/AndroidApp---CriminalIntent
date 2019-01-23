package com.criminalintent.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME=
            "com.criminalintent.time";

    private Date mDate;

    public static TimePickerFragment newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_TIME,date);

        TimePickerFragment fragment=new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent i=new Intent();
        i.putExtra(EXTRA_TIME,mDate);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //获取时间并设置
        mDate=(Date)getArguments().getSerializable(EXTRA_TIME);
        Calendar calendar=Calendar.getInstance();
        if(mDate!=null){calendar.setTime(mDate);}
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);
        final int second=calendar.get(Calendar.SECOND);

        View v=getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_time,null);

        TimePicker timePicker=(TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate=new GregorianCalendar(year,month,day,hourOfDay,minute,second).getTime();
                getArguments().putSerializable(EXTRA_TIME,mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();
    }
}
