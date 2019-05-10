package com.yb.uadnd.popularmovies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.database.Movie;
import com.yb.uadnd.popularmovies.network.NetworkUtils;
import com.yb.uadnd.popularmovies.network.models.Review;
import com.yb.uadnd.popularmovies.network.models.Trailer;
import com.yb.uadnd.popularmovies.viewmodels.MovieDetailViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    @BindView(R.id.iv_detail_poster) ImageView mPoster;
    @BindView(R.id.detail_title) TextView mTitle;
    @BindView(R.id.detail_ratings) TextView mRating;
    @BindView(R.id.detail_release_date) TextView mReleaseDate;
    @BindView(R.id.button_favorite) Button mFavoriteButton;
    @BindView(R.id.trailer_list) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.detail_overview) TextView mOverview;
    @BindView(R.id.reviews_list) RecyclerView mReviewsRecyclerView;

    public static final String EXTRA_JSON = "movie_json";
    private Movie mMovie;
    private MovieDetailViewModel mDetailViewModel;
    private final ArrayList<Trailer> mTrailers = new ArrayList<>();
    private final ArrayList<Review> mReviews = new ArrayList<>();
    private TrailerAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mMovie = new Gson().fromJson(intent.getStringExtra(EXTRA_JSON), Movie.class);

        mTitle.setText(mMovie.getTitle());
        String text = "* " + String.format(Locale.getDefault(),"%.1f", mMovie.getUserRating())
                + " (" + mMovie.getRatingCount() + ")";
        mRating.setText(text);
        mReleaseDate.setText(mMovie.getReleaseDate());
        mOverview.setText(mMovie.getOverview());

        if(mMovie.getPosterPath() == null) {
            Picasso.get().load((String)null).into(mPoster);
        } else {
            String posterPath = mMovie.getPosterPath().substring(1);
            URL url = NetworkUtils.buildPosterURL(posterPath);
            if (url != null) {
                String posterUrl = url.toString();
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.movie_placeholder)
                        .error(R.drawable.poster_error_placeholder)
                        .into(mPoster);
            }
        }

        initRecyclerViews();
        initViewModels();
    }

    private void initViewModels() {
        mDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        mDetailViewModel.getMovieData(mMovie, new MovieDetailViewModel.NetworkOperationCallback() {
            @Override
            public void onTrailersReceived() {
                mTrailers.clear();
                mTrailers.addAll(mDetailViewModel.getTrailers());
                Log.i(LOG_TAG, "onTrailersReceived: " + mTrailers.size());
                mTrailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReviewsReceived() {
                mReviews.clear();
                mReviews.addAll(mDetailViewModel.getReviews());
                mReviewsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerViews() {
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailerAdapter = new TrailerAdapter(mTrailers, this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewsAdapter = new ReviewsAdapter(this, mReviews);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }

    public void onClickFavorites(View view) {

    }

    public void onClickDisplayMovieId(View view) {
        Toast.makeText(this, "Movie id = " + mMovie.getMovieId(), Toast.LENGTH_LONG)
                .show();
    }
}
