package com.swapniljain.popularmovies.Utils;

import java.util.List;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swapniljain.popularmovies.Model.Review;
import com.swapniljain.popularmovies.R;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewsViewHolder> {

    private List<Review> mMovieReviewList;

    public MovieReviewAdapter(List<Review> reviews) {
        mMovieReviewList = reviews;
    }

    @NonNull
    @Override
    public MovieReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdFormovieItem = R.layout.movie_review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFormovieItem, parent, shouldAttachToParentImmediately);
        MovieReviewAdapter.MovieReviewsViewHolder viewHolder = new MovieReviewAdapter.MovieReviewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsViewHolder holder, int position) {
        Review review = mMovieReviewList.get(position);
        holder.reviewsView.setText(review.getDetail());
    }

    @Override
    public int getItemCount() {
        return mMovieReviewList.size();
    }


    class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewsView;

        public MovieReviewsViewHolder(View view) {
            super(view);
            reviewsView = (TextView)view.findViewById(R.id.movie_review);
        }
    }
}
