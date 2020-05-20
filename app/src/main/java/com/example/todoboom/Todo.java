package com.example.todoboom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todoNotes")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "edit_timestamp")
    private int edit_timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

   private boolean isDone;

    @ColumnInfo(name = "creation_timestamp")
    private int creation_timestamp;

    public int getCreation_timestamp() {
        return creation_timestamp;
    }

    public void setCreation_timestamp(int creation_timestamp) {
        this.creation_timestamp = creation_timestamp;
    }

    public int getEdit_timestamp() {
        return edit_timestamp;
    }

    public void setEdit_timestamp(int edit_timestamp) {
        this.edit_timestamp = edit_timestamp;
    }


    Todo(String content, boolean isDone) {
        this.content = content;
        this.isDone = isDone;

    }

    public boolean getIsDone()
    {
        return this.isDone;
    }

    public void setIsDone(boolean isDone)
    {
        this.isDone = isDone;
    }

}
