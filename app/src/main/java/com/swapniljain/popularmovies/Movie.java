package com.swapniljain.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String userRating;
    private String releaseDate;

    public Movie() {

    }

    public Movie(String originalTitle,
                 String posterPath,
                 String backdropPath,
                 String overview,
                 String userRating,
                 String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }
    @Override
    public String toString() {
        return "Original Title: " + originalTitle +
                "Poster Path: " + posterPath +
                "Backdrop Path: " + backdropPath +
                "Overview: " + overview +
                "User Rating: " + userRating +
                "Release Date: " + releaseDate + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() { return posterPath; }

    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }

    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

}
