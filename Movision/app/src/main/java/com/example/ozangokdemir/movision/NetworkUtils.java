package com.example.ozangokdemir.movision;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

class NetworkUtils {


    /**
     * This method checks the network connection of the device. Source: stackoverflow.com
     *
     * @return whether the device has connection or not.
     */
     static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * @param sortParam The sorting criteria, FetchMovieDataTask takes in this string and passed it into this method.
     * @param apiKey TMDB Api Key of the client that is executing this code.
     *
     * @return The api call URL that will be queried for in the doInBackground().
     */

    static URL formApiCall(String sortParam, String apiKey){


        final String BASE_URL = "https://api.themoviedb.org/3/movie/"+sortParam;
        final String PARAM_API_KEY = "api_key";
        URL searchUrl = null; // will be used in doInBackGround() method.

        /*
            Build the URL based on the user's sorting criteria.
         */

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        try {
            searchUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return searchUrl;
    }




}
