package com.swapniljain.popularmovies.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swapniljain.popularmovies.Model.Movie;
import com.swapniljain.popularmovies.Model.Trailer;
import com.swapniljain.popularmovies.R;
import com.swapniljain.popularmovies.Utils.JSONUtils;
import com.swapniljain.popularmovies.Utils.MovieAdapter;
import com.swapniljain.popularmovies.Utils.MovieTrailerAdapter;
import com.swapniljain.popularmovies.Utils.NetworkUtils;

import android.net.Uri;
import java.net.URL;

public class DetailActivity extends AppCompatActivity
        implements MovieTrailerAdapter.MovieTrailerClickListener {

    private MovieTrailerAdapter mMovieTrailerAdapter;
    private RecyclerView mMovieTrailerRecyclerView;

    Movie mMovieObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.movieBackdrop);
        TextView titleTextView = findViewById(R.id.tv_movie_title);
        TextView overviewTextView = findViewById(R.id.tv_movie_desc);
        TextView avgRatingTextView = findViewById(R.id.tv_movie_rating);
        TextView releaseDateTextView = findViewById(R.id.tv_movie_date);

        Intent parentIntent = getIntent();

        if (!parentIntent.hasExtra("MovieObject")) {
            return;
        }

        mMovieObject = (Movie) parentIntent.getParcelableExtra("MovieObject");

        // Set the image here.
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + mMovieObject.getBackdropPath())
                .into(imageView);

        // Set title, overview, average rating and release date.
        titleTextView.setText(mMovieObject.getOriginalTitle());
        overviewTextView.setText(mMovieObject.getOverview());
        avgRatingTextView.setText(mMovieObject.getUserRating());
        releaseDateTextView.setText(mMovieObject.getReleaseDate());

        new MovieReviewsTask().execute(NetworkUtils.buildMovieDetailURL(mMovieObject.getMovieID(), "reviews"));
        new MovieVideosTask().execute(NetworkUtils.buildMovieDetailURL(mMovieObject.getMovieID(), "videos"));
    }

    /// Setup movie trailer recycler view.
    private void setupMovieTrailerRecyclerView() {
        mMovieTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        mMovieTrailerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMovieTrailerRecyclerView.setLayoutManager(layoutManager);
        mMovieTrailerAdapter =  new MovieTrailerAdapter(mMovieObject.getTrailers(), this);
        mMovieTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);
    }

    /// AsyncTask to fetch Trailers.
    public class MovieVideosTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL movieUrl = urls[0];
            String response = null;
            try {
                response = NetworkUtils.getMovieData(movieUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            if (response != null && !response.equals("")) {
                mMovieObject.setTrailers(JSONUtils.parseMovieTrailers(response));

                // Populate UI.
                setupMovieTrailerRecyclerView();

            } else {
                // Handle null response here.
            }
        }
    }

    /// AsyncTask to fetch Reviews.
    public class MovieReviewsTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL movieUrl = urls[0];
            String response = null;
            try {
                response = NetworkUtils.getMovieData(movieUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null && !response.equals("")) {
                mMovieObject.setReviews(JSONUtils.parseMovieReviews(response));

                // Populate UI.

            } else {
                // Handle null response here.
            }
        }
    }

    /// Movie Trailer Click Listener.
    @Override
    public void onListItemClick(int clickedTrailerItemPosition) {
        Log.d("clickedTrailerItemPosition", "clickedTrailerItemPosition");
        // Open trailer.
        Trailer trailer = mMovieObject.getTrailers().get(clickedTrailerItemPosition);
        Uri webpage = Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
