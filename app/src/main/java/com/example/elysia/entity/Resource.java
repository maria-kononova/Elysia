package com.example.elysia.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Resource", foreignKeys = {@ForeignKey(entity = Task.class,
        parentColumns = "id",
        childColumns = "idTask",
        onDelete = ForeignKey.CASCADE)})
public class Resource {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "idTask")
    private long idTask;

    @ColumnInfo(name = "pathResource")
    private String pathResource;
    @ColumnInfo(name = "type")
    private String type;

    public Resource(long idTask, String pathResource, String type) {
        this.idTask = idTask;
        this.pathResource = pathResource;
        this.type = type;
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


    public String getPathResource() {
        return pathResource;
    }

    public void setPathResource(String pathResource) {
        this.pathResource = pathResource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
