package com.eventor.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addActivity(Activity activity);

    @Query("select * from activity")
    public List<Activity> getAllActivity();

    @Query("select name || ' - ' || UPPER(category) as name from activity order by name asc")
    public List<String> getActivityNames();

    @Query("select * from activity where category = :category")
    public List<Activity> getActivityByCategory(String category);

    @Query("select * from activity where id = :id")
    public List<Activity> getActivityById(long id);

    @Query("select count(*) from activity")
    public int getNextId();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateActivity(Activity activity);

    @Query("delete from activity")
    void removeAllActivities();
}