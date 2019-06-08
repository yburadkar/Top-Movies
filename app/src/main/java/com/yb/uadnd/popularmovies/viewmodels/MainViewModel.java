package com.yb.uadnd.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.yb.uadnd.popularmovies.MyApp;
import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.database.AppRepository;
import com.yb.uadnd.popularmovies.database.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public static final int MODE_POPULAR = 1;
    public static final int MODE_TOP_RATED = 2;
    public static final int MODE_FAVORITES = 3;
    private static final String TAG = MainViewModel.class.getSimpleName();
    private final AppRepository mRepository;
    private Context mAppContext;
    public int mPageNum;

    private final MutableLiveData<Integer> mMode = new MutableLiveData<>();

    public final LiveData<List<Movie>> mMovies;
    public MainViewModel(@NonNull Application application) {
        super(application);
        MyApp.getmIdlingResource().setIdleState(false);
        mAppContext = application.getApplicationContext();
        mRepository = AppRepository.getInstance(mAppContext);
        //Start the app in POPULAR mode and get fresh Page 1 data from TMDB API
        setMode(MODE_POPULAR);
        mPageNum = 0;
        mMovies = Transformations.switchMap(mMode, (mode) -> {
            switch (mode){
                case MODE_POPULAR: return mRepository.getAllPopularMovies();
                case MODE_TOP_RATED: return mRepository.getAllTopRatedMovies();
                case MODE_FAVORITES: return mRepository.getFavorites();
                default: return mRepository.getAllPopularMovies();
            }
        });
        fetchMoreMovies();
        Log.i(TAG, "MainViewModel: New Instance created");
    }

    public void fetchMoreMovies(){
        mPageNum++;
        Log.i(TAG, "fetchMoreMovies: Page " + mPageNum);
        mRepository.fetchMoviesAndInsertInDb(mMode.getValue(), mPageNum);
    }

    public void handleMenuItemClick(int mode) {
        if(mMode.getValue() != mode) changeMode(mode);
    }

    private void changeMode(int mode) {
        setMode(mode);
        if(mMode.getValue() != MODE_FAVORITES) {
            mRepository.deleteAllMovies();
            mPageNum = 0;
            fetchMoreMovies();
        }
    }

    private void setMode(int mode){
        mMode.setValue(mode);
    }

    public LiveData<Integer> getmMode() {
        return mMode;
    }

    public String provideTitle(){
        switch (mMode.getValue()){
            case MODE_POPULAR: return mAppContext.getString(R.string.popular_movies);
            case MODE_TOP_RATED: return mAppContext.getString(R.string.top_rated_movies);
            case MODE_FAVORITES: return mAppContext.getString(R.string.your_favorites);
            default: return mAppContext.getString(R.string.popular_movies);
        }
    }
}
