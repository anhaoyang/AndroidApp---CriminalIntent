package com.criminalintent.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
    private static final String JSON_FILENAME="filename";
    private String mFilename;

    public Photo(String mFilename) {
        this.mFilename = mFilename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename=json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws  JSONException{
        JSONObject json=new JSONObject();
        json.put(JSON_FILENAME,mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
}
