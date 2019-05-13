package com.yb.uadnd.popularmovies.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.database.Movie;
import com.yb.uadnd.popularmovies.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yb.uadnd.popularmovies.viewmodels.MainViewModel.*;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListScrollListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.error_message) TextView mErrorMessage;

    private final List<Movie> mMovies = new ArrayList<>();
    private MainViewModel mViewModel;
    private MovieAdapter mAdapter;
    private int mMode = MODE_POPULAR;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
        mActionBar = getSupportActionBar();
        if(mActionBar != null) mActionBar.setTitle(getString(R.string.popular_movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = getString(R.string.popular_movies);
        switch (id){
            case R.id.sort_by_popularity:
                mMode = MODE_POPULAR;
                title = getString(R.string.popular_movies);
                break;
            case R.id.sort_by_rating:
                mMode = MODE_TOP_RATED;
                title = getString(R.string.top_rated_movies);
                break;
            case R.id.favorites:
                mMode = MODE_FAVORITES;
                title = getString(R.string.your_favorites);
                break;
        }
        mViewModel.handleMenuItemClick(mMode);
        if(mActionBar != null) mActionBar.setTitle(title);
        return super.onOptionsItemSelected(item);
    }

    private void initViewModel() {
        final Observer<List<Movie>> movieObserver = movies -> {
            Log.i(TAG, "movieObserver: onChanged " + movies.size());
            mMovies.clear();
            mMovies.addAll(movies);
            mAdapter.notifyDataSetChanged();
            showEmptyMessage(movies.size() == 0);
        };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mMovies.observe(this, movieObserver);
    }

    private void showEmptyMessage(boolean show) {
        if(mMode == MODE_FAVORITES && show){
            mErrorMessage.setText(getResources().getString(R.string.favorites_empty_message));
            mErrorMessage.setVisibility(View.VISIBLE);
        }else {
            mErrorMessage.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        int spanCount = Integer.parseInt(getResources().getString(R.string.gridSpanCount));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(mMovies, this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void loadMoreMovies() {
        mViewModel.fetchMoreMovies();
    }
}
