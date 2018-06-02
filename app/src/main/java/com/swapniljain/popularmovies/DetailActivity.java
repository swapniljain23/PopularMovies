package com.swapniljain.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

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

        Movie movieObj = (Movie) parentIntent.getParcelableExtra("MovieObject");

        // Set the image here.
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + movieObj.getBackdropPath())
                .into(imageView);

        // Set title, overview, average rating and release date.
        titleTextView.setText(movieObj.getOriginalTitle());
        overviewTextView.setText(movieObj.getOverview());
        avgRatingTextView.setText(movieObj.getUserRating());
        releaseDateTextView.setText(movieObj.getReleaseDate());

    }
}
