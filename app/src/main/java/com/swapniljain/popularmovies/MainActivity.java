package com.swapniljain.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String SORT_PREFERENCE_POPULAR = "popular";
    private final static String SORT_PREFERENCE_TOP_RATED = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        String toastMessage = "";
        Context context = getApplicationContext();

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

    // AsyncTask to fetch movies.
    public class MovieTask extends AsyncTask<URL, Void, String> {
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
            if (s != null && !s.equals("")) {
                List<Movie> movies = JSONUtils.parseMovieJSON(s);
                Log.d("MovieList",movies.toString());

                // Populate UI.
                Context context =getApplicationContext();
                Toast.makeText(context, "SUCESS", Toast.LENGTH_LONG).show();
            }
        }
    }
}
