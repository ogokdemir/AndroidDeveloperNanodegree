package com.example.ozangokdemir.movision;

import android.graphics.ColorSpace;

/**
 * This is the model class.
 */

public class Movie {

    private String  mTitle;
    private String  mPosterUrl;
    private String  mOverview;
    private double  mAverageRating;
    private String  mReleaseDate;


    public Movie(String title, String posterUrl, String overview, double averageRating, String releaseDate) {
        this.mTitle = title;
        this.mPosterUrl = posterUrl;
        this.mOverview = overview;
        this.mAverageRating = averageRating;
        this.mReleaseDate = releaseDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    public void setmPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public double getmAverageRating() {
        return mAverageRating;
    }

    public void setmAverageRating(double mAverageRating) {
        this.mAverageRating = mAverageRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
