package com.swapniljain.popularmovies.Activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.swapniljain.popularmovies.Database.MovieDatabase;
import com.swapniljain.popularmovies.Model.Movie;
import com.swapniljain.popularmovies.R;
import com.swapniljain.popularmovies.Utils.JSONUtils;
import com.swapniljain.popularmovies.Utils.MovieAdapter;
import com.swapniljain.popularmovies.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private static final String SORT_PREFERENCE_POPULAR = "popular";
    private static final String SORT_PREFERENCE_TOP_RATED = "top_rated";
    private static final String MOVIE_LIST_KEY = "MOVIE_LIST";

    // Recycler view.
    private MovieAdapter mMovieAdapter;
    private RecyclerView mMovieRecyclerView;

    // Data model.
    private List<Movie> mMovieList = new ArrayList<>();

    //
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTextView;
    private TextView mNoConnectionTextView;
    private Toast mNoConnectionToast;

    private int mSelectedOptionItemId;

    /// Life cycle methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_KEY)){
            mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        }

        // Loading indicator, text view stuff.
        mLoadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
        mErrorMessageTextView = (TextView)findViewById(R.id.error_message_text_view);
        mNoConnectionTextView = (TextView)findViewById(R.id.no_connection_text_view);
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Load data.
        if (isOnline()) {
            mNoConnectionTextView.setVisibility(View.INVISIBLE);
            new MovieTask().execute(NetworkUtils.buildMovieListURL(SORT_PREFERENCE_POPULAR));
        } else {
            mNoConnectionTextView.setVisibility(View.VISIBLE);
            showNoConnectionToast();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /// Network connectivity.
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /// Recycler view stuff.
    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieAdapter =  new MovieAdapter(mMovieList, this);
        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

    /// Show
    private void showNoConnectionToast() {
        if(mNoConnectionToast != null) {
            mNoConnectionToast.cancel();
        }

        mNoConnectionToast = Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG);
        mNoConnectionToast.show();
    }

    /// Menu item selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        mSelectedOptionItemId = id;

        if (!isOnline()) {
            mNoConnectionTextView.setVisibility(View.VISIBLE);
            showNoConnectionToast();
            return true;
        }
        mNoConnectionTextView.setVisibility(View.INVISIBLE);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sortByMostPopular) {
            new MovieTask().execute(NetworkUtils.buildMovieListURL(SORT_PREFERENCE_POPULAR));
            return true;
        } else if (id == R.id.action_sortByHighestRated) {
            new MovieTask().execute(NetworkUtils.buildMovieListURL(SORT_PREFERENCE_TOP_RATED));
            return true;
        } else if (id == R.id.action_sortByFavorites) {
            showFavoriteMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    /// MovieItemClickListener implementation.
    @Override
    public void onListItemClick(int clickedMovieItemPosition) {
        // Create an intent and start activity.
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("MovieObject", (Parcelable) mMovieList.get(clickedMovieItemPosition));

        startActivity(intent);
    }

    /// AsyncTask to fetch movies.
    public class MovieTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mMovieList.clear();
            mErrorMessageTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL movieUrl = urls[0];
            String movies = null;
            try {
                movies = NetworkUtils.getMovieData(movieUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                mMovieList = JSONUtils.parseMovieJSON(s);

                // Populate UI.
                setupRecyclerView();
            }else{
                mErrorMessageTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    /// Fetch favorite movies.
    public void showFavoriteMovies() {
        mMovieList.clear();
        MovieDatabase movieDatabase = MovieDatabase.getSharedInstance(getApplicationContext());
        movieDatabase.movieDAO().loadAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (mSelectedOptionItemId == R.id.action_sortByFavorites) {
                    mMovieList = movies;
                    setupRecyclerView();
                    Log.d("showFavoriteMovies", "OnChanged called.");
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>)mMovieList);
    }
}
