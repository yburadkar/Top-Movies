package com.yb.uadnd.popularmovies.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yb.uadnd.popularmovies.database.AppRepository;
import com.yb.uadnd.popularmovies.database.Movie;
import com.yb.uadnd.popularmovies.network.models.Review;
import com.yb.uadnd.popularmovies.network.models.Trailer;

import java.util.LinkedList;

public class MovieDetailViewModel extends ViewModel {

    private static final String LOG_TAG = MovieDetailViewModel.class.getSimpleName();
    private final AppRepository mRepository;
    private LiveData<LinkedList<Trailer>> mTrailers;
    private LiveData<LinkedList<Review>> mReviews;
    public final LiveData<Integer> isFavorite;
    private final Movie mMovie;
    private int mPage = 1;

    MovieDetailViewModel(@NonNull Context appContext, Movie movie) {
        mRepository = AppRepository.getInstance(appContext);
        mMovie = movie;
        isFavorite = mRepository.isFavorite(mMovie);
        mTrailers = mRepository.getMovieTrailers(mMovie.getMovieId());
        mReviews = mRepository.getMovieReviews(mMovie.getMovieId(), mPage);
    }

    public void updateFavorites(boolean isChecked) {
        if(isChecked){
            mRepository.addToFavorites(mMovie);
        }else {
            mRepository.removeFromFavorites(mMovie);
        }
    }

    public LiveData<LinkedList<Trailer>> getTrailers() {
        return mTrailers;
    }

    public LiveData<LinkedList<Review>> getReviews() {
        return mReviews;
    }
}
