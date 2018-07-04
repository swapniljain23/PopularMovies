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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.swapniljain.popularmovies.Database.DatabaseExecutors;
import com.swapniljain.popularmovies.Database.MovieDatabase;
import com.swapniljain.popularmovies.Model.Movie;
import com.swapniljain.popularmovies.Model.Trailer;
import com.swapniljain.popularmovies.R;
import com.swapniljain.popularmovies.Utils.JSONUtils;
import com.swapniljain.popularmovies.Utils.MovieAdapter;
import com.swapniljain.popularmovies.Utils.MovieReviewAdapter;
import com.swapniljain.popularmovies.Utils.MovieTrailerAdapter;
import com.swapniljain.popularmovies.Utils.NetworkUtils;

import android.net.Uri;
import java.net.URL;

public class DetailActivity extends AppCompatActivity
        implements MovieTrailerAdapter.MovieTrailerClickListener {

    // Views.
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private RecyclerView mMovieTrailerRecyclerView;
    private MovieReviewAdapter mMovieReviewAdapter;
    private RecyclerView mMovieReviewsRecyclerView;
    private Button mFavoriteButton;

    // DB stuff.
    private SharedPreferences mSharedPreference;
    private MovieDatabase mMovieDatabase;
    private static final String FAVORITE_MOVIES = "favorite_movies";

    // Model object.
    private Movie mMovieObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get shared preference.
        mSharedPreference = getApplicationContext().getSharedPreferences(FAVORITE_MOVIES,Context.MODE_PRIVATE);

        // Setup database.
        mMovieDatabase = MovieDatabase.getSharedInstance(getApplicationContext());

        // Find views by Id.
        ImageView imageView = findViewById(R.id.movieBackdrop);
        TextView titleTextView = findViewById(R.id.tv_movie_title);
        TextView overviewTextView = findViewById(R.id.tv_movie_desc);
        TextView avgRatingTextView = findViewById(R.id.tv_movie_rating);
        TextView releaseDateTextView = findViewById(R.id.tv_movie_release_date);

        // Find favorite button by id and set on click listener.
        mFavoriteButton = findViewById(R.id.btn_favorite);
        mFavoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreference.edit();
                if (mFavoriteButton.getText().toString().equals(getString(R.string.add_to_favorite))){
                    addToFavorite(editor);
                }else if (mFavoriteButton.getText().toString().equals(getString(R.string.remove_from_favorite))){
                    removeFromFavorite(editor);
                }
            }
        });

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
        if (mSharedPreference.contains(mMovieObject.getOriginalTitle())) {
            mFavoriteButton.setText(R.string.remove_from_favorite);
        } else {
            mFavoriteButton.setText(R.string.add_to_favorite);
        }

        new MovieReviewsTask().execute(NetworkUtils.buildMovieDetailURL(mMovieObject.getMovieID(), "reviews"));
        new MovieVideosTask().execute(NetworkUtils.buildMovieDetailURL(mMovieObject.getMovieID(), "videos"));
    }

    /// Setup movie trailer recycler view.
    private void setupMovieTrailerRecyclerView() {
        mMovieTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        mMovieTrailerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMovieTrailerRecyclerView.setLayoutManager(layoutManager);
        mMovieTrailerAdapter =  new MovieTrailerAdapter(mMovieObject.getTrailers(), this);
        mMovieTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);
    }

    /// Setup movie reviews recycler view.
    private void setupMovieReviewsRecyclerView() {
        mMovieReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        mMovieReviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMovieReviewsRecyclerView.setLayoutManager(layoutManager);
        mMovieReviewAdapter =  new MovieReviewAdapter(mMovieObject.getReviews());
        mMovieReviewsRecyclerView.setAdapter(mMovieReviewAdapter);
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
                setupMovieReviewsRecyclerView();
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

    // Add/Remove faovrites.
    public void addToFavorite(SharedPreferences.Editor editor){
        DatabaseExecutors.getSharedInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDatabase.movieDAO().insertMovie(mMovieObject);
            }
        });
        mFavoriteButton.setText(R.string.remove_from_favorite);
        editor.putString(mMovieObject.getOriginalTitle(), "true");
        editor.commit();
    }

    private void removeFromFavorite(SharedPreferences.Editor editor) {
        DatabaseExecutors.getSharedInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDatabase.movieDAO().deleteMovie(mMovieObject);
            }
        });
        mFavoriteButton.setText(R.string.add_to_favorite);
        editor.remove(mMovieObject.getOriginalTitle());
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
