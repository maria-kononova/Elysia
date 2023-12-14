package com.example.elysia.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note", foreignKeys = {@ForeignKey(entity = Task.class,
        parentColumns = "id",
        childColumns = "idTask",
        onDelete = ForeignKey.CASCADE)})
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "idTask")
    private long idTask;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "dateLastChange")
    private String dateLastChange;

    public Note(long idTask, String content, String dateLastChange) {
        this.idTask = idTask;
        this.content = content;
        this.dateLastChange = dateLastChange;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTask() {
        return idTask;
    }

    public void setIdTask(long idTask) {
        this.idTask = idTask;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateLastChange() {
        return dateLastChange;
    }

    public void setDateLastChange(String dateLastChange) {
        this.dateLastChange = dateLastChange;
    }
}
