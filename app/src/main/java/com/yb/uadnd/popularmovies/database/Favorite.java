package com.yb.uadnd.popularmovies.database;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorites")
public class Favorite extends Movie {

    @Ignore
    public Favorite() {
    }

    public Favorite(int rowId, int movieId, String title, String posterPath, String overview, float userRating, float popularity, int ratingCount, String releaseDate) {
        super(rowId, movieId, title, posterPath, overview, userRating, popularity, ratingCount, releaseDate);
    }

    @Ignore
    public Favorite(int movieId, String title, String posterPath, String overview, float userRating, float popularity, int ratingCount, String releaseDate) {
        super(movieId, title, posterPath, overview, userRating, popularity, ratingCount, releaseDate);
    }

}
