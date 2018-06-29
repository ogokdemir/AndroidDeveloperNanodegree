package com.example.ozangokdemir.movision;


import android.net.Uri;
import android.util.Log;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;


/**
 * This is the model class.
 * @author Ozan Gokdemir
 */


/*
    I'm using the Parceler 3rd party library for passing movie objects within Bundles between activities.
 */


public class Movie {

    private static final String TAG = Movie.class.getSimpleName();

    //Setting the serialized names for processing the raw JSON using GSON library.

    @SerializedName(JsonUtils.JSON_TITLE_KEY)
    String  mTitle;
    @SerializedName(JsonUtils.JSON_POSTER_URI_KEY)
    String  mPosterUri;
    @SerializedName(JsonUtils.JSON_OVERVIEW_KEY)
    String  mOverview;
    @SerializedName(JsonUtils.JSON_AVG_RATING_KEY)
    double  mAverageRating;
    @SerializedName(JsonUtils.JSON_RELEASE_DATE_KEY)
    String  mReleaseDate;



    public Movie(String mTitle,
                 String mPosterUri, String mOverview,
                 double mAverageRating, String mReleaseDate) {


        this.mTitle = mTitle;
        this.mPosterUri = mPosterUri;
        this.mOverview = mOverview;
        this.mAverageRating = mAverageRating;
        this.mReleaseDate = mReleaseDate;
    }


    // Getters for UI inflation from the model Movie objects. Setting will be handled by the GSON library so no setters needed.

    public String getmTitle() {
        return mTitle;
    }


     /*
        This method is tricky. The API call returns only the relative path. It should be completed into
        an absolute path before being returned!!
     */

    public Uri getmPosterUri() {

        final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

        Uri posterUri = Uri.parse(IMAGE_BASE_URL+mPosterUri);

        Log.d(TAG, "Uri for Picasso to load the image: "+ posterUri.toString());

        return posterUri;
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
