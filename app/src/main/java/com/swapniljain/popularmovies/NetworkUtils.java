package com.swapniljain.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String API_KEY = "";

    /// Build url with the sort preference.
    public static URL buildURL (String sortPreference) {
        Uri buildUri = Uri.parse(MOVIE_URL + sortPreference).buildUpon().
                appendQueryParameter("api_key", API_KEY).
                appendQueryParameter("language", "en-US").
                appendQueryParameter("page", "1").build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        Log.d("URL:",url.toString());
        return url;
    }

    // Fetch movie list with the url.
    public static String getMovieList(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String movieList = "";
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            while (scanner.hasNext()) {
                movieList += scanner.next();
            }
            Log.d("MovieText",movieList);
        } finally {
            connection.disconnect();
        }
        return movieList;
    }

}
