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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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

        Log.i(TAG, "Call: " + call.request().url().toString());
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                Log.i(TAG, "TMdB response message: " + response.message());
                MovieApiResponse movieApiResponse = response.body();
                if (movieApiResponse != null) {
                    List<Movie> movies = movieApiResponse.getResults();
                    if(movies != null && movies.size() > 0){
                        Log.i("Network", "Got movies from TMdB = " + movies.size());
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

        Log.i(TAG, "Call: " + call.request().url().toString());
        call.enqueue(new Callback<TrailerApiResponse>() {
            @Override
            public void onResponse(Call<TrailerApiResponse> call, Response<TrailerApiResponse> response) {
                Log.i(TAG, "Trailer response message: " + response.message());
                TrailerApiResponse apiResponse = response.body();
                if (apiResponse != null) {
//                    List<Trailer> trailers = apiResponse.getResults();
                    LinkedList<Trailer> trailers = new LinkedList<>(apiResponse.getResults());
                    Log.i("Network", "Got videos from TMdB = " + trailers.size());
                    LinkedList<Trailer> validTrailers = new LinkedList<>();
                    for(int i = 0; i < trailers.size(); i++){
                        Trailer trailer = trailers.get(i);
                        if(trailer.getType().equals("Trailer") && trailer.getSite().equals("YouTube")){
                            validTrailers.add(trailer);
                        }else {
                            Log.i("Network", "Trailer = " + trailer.getName());
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

        Log.i(TAG, "Call: " + call.request().url().toString());
        call.enqueue(new Callback<ReviewApiResponse>() {
            @Override
            public void onResponse(Call<ReviewApiResponse> call, Response<ReviewApiResponse> response) {
                Log.i(TAG, "Trailer response message: " + response.message());
                ReviewApiResponse apiResponse = response.body();
                if (apiResponse != null) {
//                    List<Trailer> trailers = apiResponse.getResults();
                    LinkedList<Review> reviews = new LinkedList<>(apiResponse.getResults());
                    Log.i("Network", "Got reviews from TMdB = " + reviews.size());
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
        return null;
    }

    public int getMovieCount(){
        return mDb.movieDao().getMovieCount();
    }
}
