package com.yb.uadnd.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yb.uadnd.popularmovies.BuildConfig;
import com.yb.uadnd.popularmovies.network.models.MovieApiResponse;
import com.yb.uadnd.popularmovies.network.TmdbRetrofitInterface;
import com.yb.uadnd.popularmovies.network.models.Review;
import com.yb.uadnd.popularmovies.network.models.ReviewApiResponse;
import com.yb.uadnd.popularmovies.network.models.Trailer;
import com.yb.uadnd.popularmovies.network.models.TrailerApiResponse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yb.uadnd.popularmovies.viewmodels.MainViewModel.MODE_POPULAR;
import static com.yb.uadnd.popularmovies.viewmodels.MainViewModel.MODE_TOP_RATED;

public class AppRepository {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String TAG = AppRepository.class.getSimpleName();
    private final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static AppRepository ourInstance;
    private final AppDatabase mDb;
    private final Retrofit mRetrofit;
    private final TmdbRetrofitInterface mApiService;
    private final Executor mExecutor;

    public interface OnGetTrailersCallback{
        void onSuccess(LinkedList<Trailer> trailers);
        void onFailure(Throwable t);
    }

    public interface OnGetReviewsCallback{
        void onSuccess(LinkedList<Review> reviews);
        void onFailure(Throwable t);
    }

    public static AppRepository getInstance(Context appContext) {
        if(ourInstance == null){
            ourInstance = new AppRepository(appContext);
        }
        return ourInstance;
    }

    private AppRepository(Context appContext) {
        mDb = AppDatabase.getInstance(appContext);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = mRetrofit.create(TmdbRetrofitInterface.class);
        mExecutor = Executors.newSingleThreadExecutor();
        deleteAllMovies();
        Log.i(TAG, "AppRepository: New Repository created");
    }

    public void fetchMoviesAndInsertInDb(int mode, int pageNum) {

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("language", "en-US");
        options.put("page", Integer.toString(pageNum));

        Call<MovieApiResponse> call = null;
        switch (mode){
            case MODE_POPULAR: call = mApiService.getPopularMovies(options); break;
            case MODE_TOP_RATED: call = mApiService.getTopRatedMovies(options); break;
        }

        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                if (movieApiResponse != null) {
                    List<Movie> movies = movieApiResponse.getResults();
                    if(movies != null && movies.size() > 0){
                        insertMovies(movies);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {
                Log.i(TAG, "Failed to get movies from TMdB: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void getMovieTrailers(int id, OnGetTrailersCallback callback) {

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("language", "en-US");

        Call<TrailerApiResponse> call = mApiService.getMovieVideoList(id, options);

        call.enqueue(new Callback<TrailerApiResponse>() {
            @Override
            public void onResponse(Call<TrailerApiResponse> call, Response<TrailerApiResponse> response) {
                TrailerApiResponse apiResponse = response.body();
                if (apiResponse != null) {
                    LinkedList<Trailer> trailers = new LinkedList<>(apiResponse.getResults());
                    LinkedList<Trailer> validTrailers = new LinkedList<>();
                    for(int i = 0; i < trailers.size(); i++){
                        Trailer trailer = trailers.get(i);
                        if(trailer.getType().equals("Trailer") && trailer.getSite().equals("YouTube")){
                            validTrailers.add(trailer);
                        }
                    }
                    callback.onSuccess(validTrailers);
                }
            }
            @Override
            public void onFailure(Call<TrailerApiResponse> call, Throwable t) {
                Log.i(TAG, "Failed to get trailers from TMdB: " + t.getMessage());
                callback.onFailure(t);
            }
        });
    }

    public void getMovieReviews(int id, int page, OnGetReviewsCallback callback) {
        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("language", "en-US");
        options.put("page", Integer.toString(page));

        Call<ReviewApiResponse> call = mApiService.getReviewsList(id, options);

        call.enqueue(new Callback<ReviewApiResponse>() {
            @Override
            public void onResponse(Call<ReviewApiResponse> call, Response<ReviewApiResponse> response) {
                ReviewApiResponse apiResponse = response.body();
                if (apiResponse != null) {
                    LinkedList<Review> reviews = new LinkedList<>(apiResponse.getResults());
                    callback.onSuccess(reviews);
                }
            }
            @Override
            public void onFailure(Call<ReviewApiResponse> call, Throwable t) {
                Log.i(TAG, "Failed to get trailers from TMdB: " + t.getMessage());
                callback.onFailure(t);
            }
        });
    }

    private void insertMovies(List<Movie> movies) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovies(movies);
            }
        });
    }

    public void deleteAllMovies() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteAllMovies();
            }
        });
    }

    public LiveData<List<Movie>> getAllPopularMovies(){
        return mDb.movieDao().getPopularMovies();
    }

    public LiveData<List<Movie>> getAllTopRatedMovies() {
        return mDb.movieDao().getTopRatedMovies();
    }

    public LiveData<List<Movie>> getFavorites() {
        return mDb.favoriteDao().getAllFavorites();
    }

    public LiveData<Integer> isFavorite(Movie movie) {
        return mDb.favoriteDao().isFavorite(movie.getMovieId());
    }

    public void removeFromFavorites(Movie movie) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().removeFavorite(new Favorite(movie));
            }
        });
    }

    public void addToFavorites(Movie movie) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertFavorite(new Favorite(movie));
            }
        });
    }

    public int getMovieCount(){
        return mDb.movieDao().getMovieCount();
    }
}
