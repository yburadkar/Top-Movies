package com.yb.uadnd.popularmovies.database;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorites")
class Favorite extends Movie {

    @Ignore
    public Favorite() {
        super();
    }

    @Ignore
    public Favorite(Movie movie){
        super(movie.getRowId(), movie.getMovieId(), movie.getTitle(), movie.getPosterPath(),
                movie.getOverview(), movie.getUserRating(), movie.getPopularity(),
                movie.getRatingCount(), movie.getReleaseDate());
    }

    public Favorite(int rowId, int movieId, String title, String posterPath, String overview, float userRating, float popularity, int ratingCount, String releaseDate) {
        super(rowId, movieId, title, posterPath, overview, userRating, popularity, ratingCount, releaseDate);
    }

    @Ignore
    public Favorite(int movieId, String title, String posterPath, String overview, float userRating, float popularity, int ratingCount, String releaseDate) {
        super(movieId, title, posterPath, overview, userRating, popularity, ratingCount, releaseDate);
    }

}
