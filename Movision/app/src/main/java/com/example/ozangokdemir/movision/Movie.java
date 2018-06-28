package com.example.ozangokdemir.movision;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

import com.google.gson.annotations.SerializedName;

/**
 * This is the model class.
 * @author Ozan Gokdemir
 */

public class Movie {

    //Setting the serialized names for processing the raw JSON using GSON library.

    @SerializedName(JsonUtils.JSON_TITLE_KEY)
    private String  mTitle;
    @SerializedName(JsonUtils.JSON_POSTER_URL_KEY)
    private String  mPosterUrl;
    @SerializedName(JsonUtils.JSON_OVERVIEW_KEY)
    private String  mOverview;
    @SerializedName(JsonUtils.JSON_AVG_RATING_KEY)
    private double  mAverageRating;
    @SerializedName(JsonUtils.JSON_RELEASE_DATE_KEY)
    private String  mReleaseDate;



    public Movie(String title,
                 String posterUrl, String overview,
                 double averageRating, String releaseDate) {



        this.mTitle = title;
        this.mPosterUrl = posterUrl;
        this.mOverview = overview;
        this.mAverageRating = averageRating;
        this.mReleaseDate = releaseDate;
    }


    // Getters for UI inflation from the model Movie objects. Setting will be handled by the GSON library so no setters needed.

    public String getmTitle() {
        return mTitle;
    }


    public String getmPosterUrl() {
        return mPosterUrl;
    }


    public String getmOverview() {
        return mOverview;
    }


    public double getmAverageRating() {
        return mAverageRating;
    }


    public String getmReleaseDate() {
        return mReleaseDate;
    }


}
