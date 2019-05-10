package com.yb.uadnd.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yb.uadnd.popularmovies.database.AppRepository;
import com.yb.uadnd.popularmovies.database.Movie;
import com.yb.uadnd.popularmovies.network.models.Review;
import com.yb.uadnd.popularmovies.network.models.Trailer;

import java.util.LinkedList;

public class MovieDetailViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MovieDetailViewModel.class.getSimpleName();
    private final AppRepository mRepository;
    private LinkedList<Trailer> mTrailers = new LinkedList<>();
    private LinkedList<Review> mReviews = new LinkedList<>();
    private Movie mMovie;
    private int mPage = 1;

    public interface NetworkOperationCallback {
        void onTrailersReceived();
        void onReviewsReceived();
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void getMovieData(Movie movie, NetworkOperationCallback callback){
        mMovie = movie;
        if(mMovie != null){
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
    }

    public LinkedList<Trailer> getTrailers() {
        return mTrailers;
    }

    public LinkedList<Review> getReviews() {
        return mReviews;
    }
}
