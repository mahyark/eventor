package com.eventor;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eventor.model.Activity;
import com.eventor.model.ActivityDao;

@Database(entities = {Activity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ActivityDao activityDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "activitydatabase")
//Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // To simplify the exercise, allow queries on the main thread.
                            // Don't do this on a real app!
                            .allowMainThreadQueries()
                            // recreate the database if necessary
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}