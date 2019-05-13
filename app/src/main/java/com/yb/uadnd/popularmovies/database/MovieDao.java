package com.yb.uadnd.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

//    @Query("SELECT * FROM popular ORDER BY popularity DESC")
    @Query("SELECT * FROM popular")
    LiveData<List<Movie>> getPopularMovies();

//    @Query("SELECT * FROM popular ORDER BY userRating DESC")
    @Query("SELECT * FROM popular")
    LiveData<List<Movie>> getTopRatedMovies();

    @Query("DELETE FROM popular ")
    void deleteAllMovies();

    @Query("SELECT COUNT(*) FROM popular")
    int getMovieCount();
}
