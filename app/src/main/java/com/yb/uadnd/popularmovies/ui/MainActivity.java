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

import com.yb.uadnd.popularmovies.MyApp;
import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.SimpleIdlingResource;
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
    private int mMode;
    private ActionBar mActionBar;
    private SimpleIdlingResource mResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
        mActionBar = getSupportActionBar();
        mResource = MyApp.getmIdlingResource();
    }

    private void initRecyclerView() {
        int spanCount = Integer.parseInt(getResources().getString(R.string.gridSpanCount));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(mMovies, this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViewModel() {
        final Observer<List<Movie>> movieObserver = movies -> {
            Log.i(TAG, "movieObserver: onChanged " + movies.size());
            mMovies.clear();
            mMovies.addAll(movies);
            mAdapter.notifyDataSetChanged();
            showEmptyMessage(movies.size() == 0);
            if(mResource != null && movies.size()>0) mResource.setIdleState(true);
        };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mMovies.observe(this, movieObserver);
        mViewModel.getmMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setActionBarTitle();
            }
        });
    }

    private void showEmptyMessage(boolean show) {
        if(mMode == MODE_FAVORITES && show){
            mErrorMessage.setText(getResources().getString(R.string.favorites_empty_message));
            mErrorMessage.setVisibility(View.VISIBLE);
        }else {
            mErrorMessage.setVisibility(View.GONE);
        }
    }

    @Override
    //Movie Adapter triggers this method when user scrolls nearer to the end of the current page
    public void loadMoreMovies() {
        mViewModel.fetchMoreMovies();
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
        switch (id){
            case R.id.sort_by_popularity:
                mMode = MODE_POPULAR;
                break;
            case R.id.sort_by_rating:
                mMode = MODE_TOP_RATED;
                break;
            case R.id.favorites:
                mMode = MODE_FAVORITES;
                break;
        }
        mViewModel.handleMenuItemClick(mMode);
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle() {
        String title = mViewModel.provideTitle();
        if(mActionBar != null) mActionBar.setTitle(title);
    }
}
