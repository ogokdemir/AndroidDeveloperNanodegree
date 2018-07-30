package com.example.ozangokdemir.movision.models;


import android.net.Uri;
import android.util.Log;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the model class.
 * @author Ozan Gokdemir
 */


/*
    I'm using the Parceler 3rd party library for passing movie objects within Bundles between activities.
 */

@Parcel
public class Movie {

    private static final String TAG = Movie.class.getSimpleName();

    //Using @SerializedName for the GSON library to parse the raw json data into this object.

    @SerializedName("id")
    int mId;
    @SerializedName("title")
    String  mTitle;
    @SerializedName("poster_path")
    String  mPosterUri;
    @SerializedName("overview")
    String  mOverview;
    @SerializedName("vote_average")
    double  mAverageRating;
    @SerializedName("release_date")
    String  mReleaseDate;
    @SerializedName("adult")
    boolean mAdult;
    @SerializedName("genre_ids")
    List<Integer> mGenreIds = new ArrayList<Integer>();
    @SerializedName("original_title")
    String mOriginalTitle;
    @SerializedName("original_language")
    String mOriginalLanguage;
    @SerializedName("backdrop_path")
    String mBackdropPath;
    @SerializedName("popularity")
    Double mPopularity;
    @SerializedName("vote_count")
    Integer mVoteCount;
    @SerializedName("video")
    Boolean mVideo;






    /*
        Default constructor for the Parceler annotation library. It's a requirement of the 3rd party library.
    */

    public Movie() { }

    public Movie(int id, String title, String posterUri,
                 String overview, double averageRating, String releaseDate,
                 boolean adult, List<Integer> genreIds, String originalTitle,
                 String originalLanguage, String backdropPath, Double popularity,
                 Integer voteCount, Boolean video) {

        mId = id;
        mTitle = title;
        mPosterUri = posterUri;
        mOverview = overview;
        mAverageRating = averageRating;
        mReleaseDate = releaseDate;
        mAdult = adult;
        mGenreIds = genreIds;
        mOriginalTitle = originalTitle;
        mOriginalLanguage = originalLanguage;
        mBackdropPath = backdropPath;
        mPopularity = popularity;
        mVoteCount = voteCount;
        mVideo = video;
    }


// Getters for UI inflation from the model Movie objects. Setting will be handled by the GSON library so no setters needed.

    public String getTitle() {
        return mTitle;
    }


     /*
        This method is tricky. The API call returns only the relative path. It should be completed into
        an absolute path before being returned.
     */

    public Uri getPosterUri() {

        final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

        Uri posterUri = Uri.parse(IMAGE_BASE_URL+mPosterUri);

        Log.d(TAG, "Uri for Picasso to load the image: "+ posterUri.toString());

        return posterUri;
    }


    public String getOverview() {
        return mOverview;
    }


    public double getAverageRating() {
        return mAverageRating;
    }


    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getId() {return mId;}


    public boolean ismAdult() {
        return mAdult;
    }

    public List<Integer> getmGenreIds() {
        return mGenreIds;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public Double getmPopularity() {
        return mPopularity;
    }

    public Integer getmVoteCount() {
        return mVoteCount;
    }

    public Boolean getmVideo() {
        return mVideo;
    }
}
