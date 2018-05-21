package com.swapniljain.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private final static String SORT_PREFERENCE_POPULAR = "popular";
    private final static String SORT_PREFERENCE_TOP_RATED = "top_rated";

    // Recycler view.
    private MovieAdapter mMovieAdapter;
    private RecyclerView mMovieRecyclerView;

    // Data.
    private List<Movie> mMovieList = new ArrayList<>();

    //
    private Toast mOnClickToast;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Loading indicator stuff.
        mLoadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
    }

    private void setupRecyclerView() {
        // Recycler view stuff.
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieAdapter =  new MovieAdapter(mMovieList, this);
        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sortByMostPopular) {
            new MovieTask().execute(NetworkUtils.buildURL(SORT_PREFERENCE_POPULAR));
            return true;
        } else if (id == R.id.action_sortByHighestRated) {
            new MovieTask().execute(NetworkUtils.buildURL(SORT_PREFERENCE_TOP_RATED));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedMovieItemPosition) {
        if (mOnClickToast != null) {
            mOnClickToast.cancel();
        }
        String toastMessage = "Movie item #" + clickedMovieItemPosition + "clicked.";
        mOnClickToast.setText(toastMessage);
        mOnClickToast.show();
    }

    // AsyncTask to fetch movies.
    public class MovieTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mMovieList.clear();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL movieUrl = urls[0];
            String movies = null;
            try {
                movies = NetworkUtils.getMovieList(movieUrl);
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
                Log.d("MovieList",mMovieList.toString());

                // Populate UI.
                //mMovieAdapter.notifyDataSetChanged();
                setupRecyclerView();

                // Display message.
                Context context =getApplicationContext();
                Toast.makeText(context, "SUCESS", Toast.LENGTH_LONG).show();
            }
        }
    }
}
