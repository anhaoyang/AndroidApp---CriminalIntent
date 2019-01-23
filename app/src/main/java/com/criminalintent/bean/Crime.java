package com.criminalintent.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID id;
    private String title;
    private Date date;
    private Photo mPhoto;
    private boolean solved;
    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";

    public Crime() {
        id=UUID.randomUUID();
    }

    public Crime(JSONObject json) throws JSONException{
        id=UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)){
            title=json.getString(JSON_TITLE);
        }
        if(json.has(JSON_SOLVED)){
            solved=json.getBoolean(JSON_SOLVED);
        }
        if(json.has(JSON_DATE)){
            date=new Date(json.getLong(JSON_DATE));
        }
        if(json.has(JSON_PHOTO)){
            mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Photo getPhoto() { return mPhoto;}

    public void setPhoto(Photo photo) { this.mPhoto = photo;}

    public JSONObject toJSON() throws JSONException{
        JSONObject json=new JSONObject();
        json.put(JSON_ID,id.toString());
        json.put(JSON_TITLE,title);
        json.put(JSON_SOLVED,solved);
        json.put(JSON_DATE,date==null?null:date.getTime());
        json.put(JSON_PHOTO,mPhoto==null?null:mPhoto.toJSON());
        return json;
    }

    @Override
    public String toString() {
        return title;
    }
}
