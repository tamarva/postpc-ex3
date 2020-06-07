package com.example.todoboom;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;

public class Todo implements Parcelable {
    public String id;
    public String content;
    public boolean completed;
    public String createdOn;
    public String lastEdit;

    public Todo() {
    }

    public Todo(String id, String text, boolean isDone) {
        this.content = text;
        this.completed = isDone;
        lastEdit = createdOn;
        this.id = id;
        createdOn = getCreationTime();

    }
    public Todo(DocumentSnapshot document) {
        this.content = (String) document.get("text");
        this.completed = document.getBoolean("completed");
        createdOn = (String) document.get("createdOn");
        lastEdit = (String) document.get("lastEdit");
        this.id = (String) document.get("id");
    }

    public Todo(String id, String content) {
        this(id, content, false);
    }

    protected Todo(Parcel in) {
        id = in.readString();
        content = in.readString();
        completed = in.readByte() != 0;
        createdOn = in.readString();
        lastEdit = in.readString();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getContent() {
        return content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setContent(String content) {
        this.content = content;
        setLastEdit(getCreationTime());
    }


    public void setId(String id) {
        this.id = id;
        setLastEdit(getCreationTime());
    }


    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        setLastEdit(getCreationTime());
    }


    public void setIsDone(boolean isDone) {
        this.completed = isDone;
        setLastEdit(getCreationTime());
    }

    public void setLastEdit(String lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getId() {
        return id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getLastEdit() {
        return lastEdit;
    }


    public String getCreationTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        String  creation_timestamp = dateFormat.format(new Date()); // Find todays date

        return creation_timestamp;

    }


    public static String getMonth(String monthNum){
        switch(monthNum){
            case "01":{
                return "Jan";
            }
            case "02":{
                return "Feb";
            }
            case "03":{
                return "Mar";
            }
            case "04":{
                return "Apr";
            }
            case "05":{
                return "May";
            }
            case "06":{
                return "Jun";
            }
            case "07":{
                return "Jul";
            }
            case "08":{
                return "Aug";
            }
            case "09":{
                return "Sep";
            }
            case "10":{
                return "Oct";
            }
            case "11":{
                return "Nov";
            }
            case "12":{
                return "Dec";
            }

            default:{
                return "Error";
            }
        }
    }



    public void changeDone() {
        setIsDone(!isCompleted());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(createdOn);
        dest.writeString(lastEdit);
    }

    public Map<String, Object> myData() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("content", content);
        data.put("completed", completed);
        data.put("createdOn", createdOn);
        data.put("lastEdit", lastEdit);
        return data;
    }

}
