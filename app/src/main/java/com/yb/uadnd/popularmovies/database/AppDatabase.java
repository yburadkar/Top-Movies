package com.yb.uadnd.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, Favorite.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract MovieDao movieDao();
    public abstract FavoriteDao favoriteDao();

    static AppDatabase getInstance(Context context) {
        if(instance == null){
            synchronized (LOCK){
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, DATABASE_NAME).build();
                    Log.i(TAG, "getInstance: New Db instance created");
                }
            }
        }
        return instance;
    }
}
