package com.swapniljain.popularmovies.Utils;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swapniljain.popularmovies.Model.Trailer;
import com.swapniljain.popularmovies.R;

import org.w3c.dom.Text;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private List<Trailer> mMovieTrailerList;
    final private MovieTrailerClickListener mOnClickListener;

    public MovieTrailerAdapter(List<Trailer> trailers, MovieTrailerClickListener onClickListener) {
        mMovieTrailerList = trailers;
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdFormovieItem = R.layout.movie_trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdFormovieItem, parent, shouldAttachToParentImmediately);
        MovieTrailerAdapter.MovieTrailerViewHolder viewHolder = new MovieTrailerAdapter.MovieTrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        Trailer trailer = mMovieTrailerList.get(position);
        holder.trailerView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return mMovieTrailerList.size();
    }

    public interface MovieTrailerClickListener {
        public void onListItemClick(int clickedTrailerItemPosition);
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView trailerView;

        public MovieTrailerViewHolder(View view) {
            super(view);
            trailerView = (TextView)view.findViewById(R.id.movie_trailer);
            trailerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
