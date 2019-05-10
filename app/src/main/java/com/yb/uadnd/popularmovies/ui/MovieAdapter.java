package com.yb.uadnd.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.database.Movie;
import com.yb.uadnd.popularmovies.network.NetworkUtils;
import com.yb.uadnd.popularmovies.viewmodels.MainViewModel;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie>mMovies;
    private final Context mContext;
    private MainViewModel mViewModel;
    private final MovieListScrollListener mMovieListScrollListener;

    public interface MovieListScrollListener{
        void loadMoreMovies();
    }

    MovieAdapter(List<Movie> mMovies, Context mContext, MovieListScrollListener listener) {
        this.mMovies = mMovies;
        this.mContext = mContext;
        this.mMovieListScrollListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mTitle.setText(movie.getTitle());
        String ratingText = String.format(Locale.getDefault(),"%.1f",movie.getUserRating())
                + " (" + movie.getRatingCount() + ")       " + (position + 1);
        holder.mRating.setText(ratingText);
        if(movie.getPosterPath() == null) {
            Picasso.get().load((String)null).into(holder.mPoster);
        } else {
            String posterPath = movie.getPosterPath().substring(1);
            URL url = NetworkUtils.buildPosterURL(posterPath);
            if(url != null) {
                String posterUrl = url.toString();
                Picasso.get()
                        .load(posterUrl)
                        .placeholder(R.drawable.movie_placeholder)
                        .error(R.drawable.poster_error_placeholder)
                        .into(holder.mPoster);
            }
        }
        if(position == (mMovies.size() - 10)){
            mMovieListScrollListener.loadMoreMovies();
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster) ImageView mPoster;
        @BindView(R.id.tv_movie_title) TextView mTitle;
        @BindView(R.id.tv_movie_rating) TextView mRating;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //Get the movie id from the ViewModel and pass it through the intent
            Movie movie = mMovies.get(position);
            String json = new Gson().toJson(movie);
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_JSON, json);
            mContext.startActivity(intent);
        }
    }
}
