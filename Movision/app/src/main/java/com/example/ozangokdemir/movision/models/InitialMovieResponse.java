package com.example.ozangokdemir.movision.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The movie data in the TMDB Api call is wrapped in a bunch of extra variables.
 *
 * This POJO contains that initial response and allows for parsing the "results" array that comes within it into a Movie model.
 */

public class InitialMovieResponse {

    @SerializedName("page")
    private int mPage;
    @SerializedName("results")
    private List<Movie> mResults;
    @SerializedName("total_results")
    private int mTotalResults;
    @SerializedName("total_pages")
    private int mTotalPages;



    public List<Movie> getResults() {
        return  mResults;
    }


}
