package com.yb.uadnd.popularmovies.ui;

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
import android.widget.ProgressBar;
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
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private final List<Movie> mMovies = new ArrayList<>();
    private MainViewModel mViewModel;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
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
        int mode = MODE_POPULAR;
        switch (id){
            case R.id.sort_by_popularity: mode = MODE_POPULAR; break;
            case R.id.sort_by_rating: mode = MODE_TOP_RATED; break;
            case R.id.favorites: mode = MODE_FAVORITES; break;
        }
        mViewModel.handleMenuItemClick(mode);
        return super.onOptionsItemSelected(item);
    }

    private void initViewModel() {
        final Observer<List<Movie>> movieObserver = movies -> {
            Log.i(TAG, "movieObserver: onChanged " + movies.size());
            mMovies.clear();
            mMovies.addAll(movies);
            mAdapter.notifyDataSetChanged();
        };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mMovies.observe(this, movieObserver);
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
