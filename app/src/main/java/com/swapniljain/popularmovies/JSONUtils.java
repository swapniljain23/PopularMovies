package com.swapniljain.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    private static final String ORIGINAL_TITLE = "original_title";
    private static final String IMAGE_THUMBNAIL = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    public static List<Movie> parseMovieJSON(String json) {
        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject object = jsonArray.getJSONObject(index);
                Movie movie = new Movie();

                String title = object.getString(ORIGINAL_TITLE);
                movie.setOriginalTitle(title);

                String imageThumbnail = object.getString(IMAGE_THUMBNAIL);
                movie.setImageThumbnail(imageThumbnail);

                String overview = object.getString(OVERVIEW);
                movie.setOverview(overview);

                String userRating = object.getString(USER_RATING);
                movie.setUserRating(userRating);

                String releaseDate = object.getString(RELEASE_DATE);
                movie.setReleaseDate(releaseDate);

                movieList.add(movie);
            }

            Log.d("JSONUtils","MovieList:" + movieList.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return movieList;
    }
}
