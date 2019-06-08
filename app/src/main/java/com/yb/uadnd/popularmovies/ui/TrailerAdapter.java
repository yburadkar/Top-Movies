package com.yb.uadnd.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.uadnd.popularmovies.R;
import com.yb.uadnd.popularmovies.network.models.Trailer;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yb.uadnd.popularmovies.network.NetworkUtils.Companion;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final ArrayList<Trailer> mTrailers;
    private final Context mContext;

    TrailerAdapter(ArrayList<Trailer> mTrailers, Context context) {
        this.mTrailers = mTrailers;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.mTrailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if(mTrailers == null) return 0;
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.trailer_name) TextView mTrailerName;
        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView );
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            URL url = Companion.buildYouTubeUrl(mTrailers.get(position).getKey());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            mContext.startActivity(intent);
        }
    }

}
