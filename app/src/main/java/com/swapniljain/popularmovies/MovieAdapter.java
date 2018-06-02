package com.swapniljain.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovieList;
    final private MovieItemClickListener mOnclickListener;

    public MovieAdapter(List<Movie> movieList, MovieItemClickListener clickListener) {
        mMovieList = movieList;
        mOnclickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdFormovieItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFormovieItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movieObj = mMovieList.get(position);
        // Set the image here.
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + movieObj.getPosterPath())
                .into(holder.movieThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public interface MovieItemClickListener {
        void onListItemClick(int clickedMovieItemPosition);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView movieThumbnailImageView;

        public MovieViewHolder(View imageView ) {
            super(imageView);
            movieThumbnailImageView = (ImageView) imageView.findViewById(R.id.movie_thumbnail);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnclickListener.onListItemClick(clickedPosition);
        }

    }
}
