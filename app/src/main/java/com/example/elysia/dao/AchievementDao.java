package com.example.elysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Resource;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AchievementDao {
    @Query("Select * from Achievement")
    List<Achievement> getAchievements();

    @Query("Select * from Achievement where id=:id")
    Achievement getAchievementById(long id);

    @Query("update Achievement set titleAchievement=:title where id =:id")
    void updateAchievement(long id, String title);

    @Insert
    void insert(Achievement achievement);

    @Update
    void update(Achievement achievement);

    @Delete
    void delete(Achievement achievement);
}
