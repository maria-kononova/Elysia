package com.example.elysia.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Achievement")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "titleAchievement")
    private String title;

    public Achievement(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
