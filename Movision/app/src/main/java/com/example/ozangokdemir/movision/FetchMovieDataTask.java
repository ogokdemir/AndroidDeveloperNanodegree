package com.example.ozangokdemir.movision;

import android.net.Uri;
import android.os.AsyncTask;

import java.net.URL;

/**
 * Parameter will decide the sort criteria - (top rank or popular)
 */

public class FetchMovieDataTask extends AsyncTask <String, Void, Movie[]> {


    private final String mClientApiKey;


    /**
     * @param apiKey Reviever's API Key will be passed here.
     */

    public FetchMovieDataTask(String apiKey){
        this.mClientApiKey = apiKey;
    }

    @Override
    protected Movie[] doInBackground(String... strings) {

        URL url =

    }

    /**
     * This method takes in the sort parameter(e.g: Top ranked, popular, now playing etc.) and builds the Api call url.
     * @param parameter
     * @return
     */
    private URL getUrlBasedOnParameter(String parameter){

        final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
        final String PARAM_SORT_BY = "sort_by";
        final String PARAM_API_KEY = "api_key";




    }

}
