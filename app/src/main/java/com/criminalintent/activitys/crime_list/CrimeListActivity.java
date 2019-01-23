package com.criminalintent.activitys.crime_list;

import android.support.v4.app.Fragment;

import com.criminalintent.activitys.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        CrimeListFragment crimeListFragment=new CrimeListFragment();
        return new CrimeListFragment();
    }
}
