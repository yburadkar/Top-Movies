package com.yb.uadnd.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Favorite movie);

    @Query("SELECT COUNT() FROM favorites WHERE movieId = :movieId")
    LiveData<Integer> isFavorite(int movieId);

    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> getAllFavorites();

    @Delete
    void removeFavorite(Favorite movie);
}
