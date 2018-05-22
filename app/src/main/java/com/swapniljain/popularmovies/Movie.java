package com.swapniljain.popularmovies;

public class Movie {

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

    @Override
    public String toString() {
        return "Original Title: " + originalTitle +
                "Poster Path: " + posterPath +
                "Backdrop Path: " + backdropPath +
                "Overview: " + overview +
                "User Rating: " + userRating +
                "Release Date: " + releaseDate + "\n";
    }

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
