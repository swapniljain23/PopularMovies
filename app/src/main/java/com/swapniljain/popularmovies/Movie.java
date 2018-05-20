package com.swapniljain.popularmovies;

public class Movie {

    private String originalTitle;
    private String imageThumbnail;
    private String overview;
    private String userRating;
    private String releaseDate;

    public Movie() {

    }

    public Movie(String originalTitle, String imageThumbnail, String overview, String userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.imageThumbnail = imageThumbnail;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return originalTitle + ">> " + imageThumbnail + ">> " + overview + ">> " + userRating + ">> " + releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
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

}
