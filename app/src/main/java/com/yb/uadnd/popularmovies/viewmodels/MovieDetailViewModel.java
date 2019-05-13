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
    private LinkedList<Trailer> mTrailers = new LinkedList<>();
    private LinkedList<Review> mReviews = new LinkedList<>();
    public final LiveData<Integer> isFavorite;
    private final Movie mMovie;
    private int mPage = 1;

    public interface NetworkOperationCallback {
        void onTrailersReceived();
        void onReviewsReceived();
    }

    MovieDetailViewModel(@NonNull Context appContext, Movie movie) {
        mRepository = AppRepository.getInstance(appContext);
        mMovie = movie;
        isFavorite = mRepository.isFavorite(mMovie);
    }

    public void getMovieData(Movie movie, NetworkOperationCallback callback){
        int id = mMovie.getMovieId();
        mRepository.getMovieTrailers(id, new AppRepository.OnGetTrailersCallback() {
            @Override
            public void onSuccess(LinkedList<Trailer> trailers) {
                Log.i(LOG_TAG, "onSuccess: Trailers Received " + trailers.size());
                mTrailers = trailers;
                callback.onTrailersReceived();
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
        mRepository.getMovieReviews(id, mPage, new AppRepository.OnGetReviewsCallback() {
            @Override
            public void onSuccess(LinkedList<Review> reviews) {
                mReviews = reviews;
                callback.onReviewsReceived();
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void toggleFavorite(boolean isChecked) {
        if(isChecked){
            mRepository.addToFavorites(mMovie);
        }else {
            mRepository.removeFromFavorites(mMovie);
        }
    }

    public LinkedList<Trailer> getTrailers() {
        return mTrailers;
    }

    public LinkedList<Review> getReviews() {
        return mReviews;
    }
}
