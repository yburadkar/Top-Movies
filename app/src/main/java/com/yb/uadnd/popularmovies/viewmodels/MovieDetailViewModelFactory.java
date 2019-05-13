package com.yb.uadnd.popularmovies.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yb.uadnd.popularmovies.database.Movie;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context mContext;
    private final Movie mMovie;

    public MovieDetailViewModelFactory(Context appContext, Movie mMovie) {
        this.mContext = appContext;
        this.mMovie = mMovie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mContext, mMovie);
    }
}
