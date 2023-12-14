package com.example.elysia.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "contentEvent")
    private String contentEvent;
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "notification")
    private Boolean notification;

    public Event(String contentEvent, String date, String time, Boolean notification) {
        this.contentEvent = contentEvent;
        this.date = date;
        this.time = time;
        this.notification = notification;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentEvent() {
        return contentEvent;
    }

    public void setContentEvent(String contentEvent) {
        this.contentEvent = contentEvent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

}
