package com.criminalintent.lab;

import android.content.Context;
import android.util.Log;

import com.criminalintent.utils.CriminalIntentJSONSerializer;
import com.criminalintent.bean.Crime;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static final String TAG="CrimeLab";
    private static final String FILENAME="crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext) {
        this.mAppContext = appContext;
        mSerializer=new CriminalIntentJSONSerializer(mAppContext,FILENAME);

        try {
            mCrimes=mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes=new ArrayList<Crime>();
            Log.d(TAG,"Error loading crimes: ",e );
        }

//        先生成100个
//        mCrimes=new ArrayList<Crime>();
//        for(int i=0;i<100;i++){
//            Crime c=new Crime();
//            c.setTitle("Crime #"+i);
//            c.setSolved(i%2==0);
//            mCrimes.add(c);
//        }
    }
    public static CrimeLab get(Context c){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime c:mCrimes){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }
    public void deleteCrime(Crime c){
        deletePhoto(c);
        mCrimes.remove(c);
    }

    public void deletePhoto(Crime c){
        //如果存在照片就删除
        if(c.getPhoto()!=null){
            if(c.getPhoto().getFilename()!=null){
                mAppContext.getFileStreamPath(c.getPhoto().getFilename()).delete();
                c.setPhoto(null);
            }
        }
    }

    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file :"+FILENAME);
            return true;
        } catch (Exception e) {
            Log.d(TAG,"Error saving crimes :",e);
            return false;
        }
    }
}
