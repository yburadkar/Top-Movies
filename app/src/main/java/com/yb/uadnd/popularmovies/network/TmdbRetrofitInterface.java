package com.yb.uadnd.popularmovies.network;

import com.yb.uadnd.popularmovies.network.models.MovieApiResponse;
import com.yb.uadnd.popularmovies.network.models.ReviewApiResponse;
import com.yb.uadnd.popularmovies.network.models.TrailerApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TmdbRetrofitInterface {

    @GET("popular")
    Call<MovieApiResponse> getPopularMovies(@QueryMap Map<String, String> options);

    @GET("top_rated")
    Call<MovieApiResponse> getTopRatedMovies(@QueryMap Map<String, String> options);

    @GET("{movie_id}/videos")
    Call<TrailerApiResponse> getMovieVideoList(
            @Path("movie_id") int movie_id, @QueryMap Map<String, String> options );

    @GET("{movie_id}/reviews")
    Call<ReviewApiResponse> getReviewsList(
            @Path("movie_id") int movie_id, @QueryMap Map<String, String> options );



}
