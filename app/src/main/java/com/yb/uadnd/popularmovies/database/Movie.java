package com.yb.uadnd.popularmovies.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "popular")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int rowId;

    @SerializedName("id")
    private int movieId;

    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    private String overview;

    @SerializedName("vote_average")
    private float userRating;

    private float popularity;

    @SerializedName("vote_count")
    private int ratingCount;

    @SerializedName("release_date")
    private String releaseDate;

    @Ignore
    public Movie() {
    }

    public Movie(int rowId, int movieId, String title, String posterPath, String overview, float userRating, float popularity, int ratingCount, String releaseDate) {
        this.rowId = rowId;
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.popularity = popularity;
        this.ratingCount = ratingCount;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Movie(int movieId, String title, String posterPath, String overview, float userRating,
                 float popularity, int ratingCount, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.popularity = popularity;
        this.ratingCount = ratingCount;
        this.releaseDate = releaseDate;
    }

    int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath.substring(1);
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
