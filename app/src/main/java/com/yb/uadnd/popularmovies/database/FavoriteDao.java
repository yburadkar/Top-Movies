package com.yb.uadnd.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao()
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Movie movie);

    @Query("SELECT * FROM favorites WHERE EXISTS(SELECT * FROM favorites WHERE movieId = :movieId )")
    boolean isFavorite(int movieId);

    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> getAllFavorites();

    @Delete
    void removeFavorite(Movie movie);
}
