package com.example.elysia.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.type.DateTime;

@Entity(tableName = "Task", foreignKeys = {@ForeignKey(entity = Achievement.class,
        parentColumns = "id",
        childColumns = "idAchievement",
        onDelete = ForeignKey.SET_DEFAULT)})
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "titleTask")
    private String title;
    @ColumnInfo(name = "descriptionTask")
    private String description;
    @ColumnInfo(name = "isDone")
    private boolean isDone;
    @ColumnInfo(name = "dateCreate")
    private String dateCreate;
    @ColumnInfo(name = "dateFinish")
    private String dateFinish;

    @ColumnInfo(name = "idAchievement", defaultValue = "1")
    private long idAchievement;


    public Task(String title, String description, String dateCreate, String dateFinish, long idAchievement) {
        this.title = title;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateFinish = dateFinish;
        this.idAchievement = idAchievement;
        this.isDone = false;
    }

    public long getIdAchievement() {
        return idAchievement;
    }

    public void setIdAchievement(long idAchievement) {
        this.idAchievement = idAchievement;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }
}