package com.yb.uadnd.popularmovies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.network.models.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private final Context mContext;
    private final ArrayList<Review> mReviews;

    ReviewsAdapter(Context mContext, ArrayList<Review> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        String authorText = "user: " + review.getAuthor();
        holder.author.setText(authorText);
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textview_author) TextView author;
        @BindView(R.id.textview_content) TextView content;
        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
