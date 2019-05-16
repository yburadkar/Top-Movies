package com.yb.uadnd.popularmovies.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import com.yb.uadnd.popularmovies.viewmodels.MovieDetailViewModelFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    @BindView(R.id.iv_detail_poster) ImageView mPoster;
    @BindView(R.id.detail_title) TextView mTitle;
    @BindView(R.id.detail_ratings) TextView mRating;
    @BindView(R.id.detail_release_date) TextView mReleaseDate;
    @BindView(R.id.button_favorite) CheckBox mFavoriteCheckbox;
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
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setTitle(getString(R.string.movie_detail_title));
    }

    private void initViewModels() {
        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(
                getApplicationContext(), mMovie);
        mDetailViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        mDetailViewModel.isFavorite.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                if(count != null) markFavorite(count > 0);
            }
        });
        mDetailViewModel.getTrailers().observe(this, new Observer<LinkedList<Trailer>>() {
            @Override
            public void onChanged(LinkedList<Trailer> trailers) {
                mTrailers.clear();
                mTrailers.addAll(trailers);
                mTrailerAdapter.notifyDataSetChanged();
            }
        });
        mDetailViewModel.getReviews().observe(this, new Observer<LinkedList<Review>>() {
            @Override
            public void onChanged(LinkedList<Review> reviews) {
                mReviews.clear();
                mReviews.addAll(reviews);
                mReviewsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void markFavorite(boolean isFavorite) {
        Log.i(LOG_TAG, "markFavorite: " + isFavorite);
        mFavoriteCheckbox.setChecked(isFavorite);
        if(isFavorite){
            mFavoriteCheckbox.setText(R.string.marked_favorite);
        }else {
            mFavoriteCheckbox.setText(R.string.favorite_button_text);
        }
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
        boolean isChecked = mFavoriteCheckbox.isChecked();
        mDetailViewModel.updateFavorites(isChecked);
    }

    public void onClickDisplayMovieId(View view) {
        Toast.makeText(this, "Movie id = " + mMovie.getMovieId(), Toast.LENGTH_LONG)
                .show();
    }
}
