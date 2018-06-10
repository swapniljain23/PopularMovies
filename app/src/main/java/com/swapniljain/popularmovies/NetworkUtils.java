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
    private final static String API_KEY = "48251e830f06126f3af569422672a6c1";

    /// Build url with the sort preference.
    public static URL buildMovieListURL(String sortPreference) {
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

    /// Build url with the sort preference.
    public static URL buildMovieDetailURL(String movieID, String request) {
        Uri buildUri = Uri.parse(MOVIE_URL + movieID + "/" + request).buildUpon().
                appendQueryParameter("api_key", API_KEY).
                appendQueryParameter("language", "en-US").build();
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
    public static String getMovieData(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String response = "";
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            while (scanner.hasNext()) {
                response += scanner.next();
            }
            Log.d("RESPONSE",response);
        } finally {
            connection.disconnect();
        }
        return response;
    }
}
