package com.example.ozangokdemir.movision;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author Ozan Gokdemir
 * This class will contain the code for processing JSON.
 */
public class JsonUtils {

    // These are keys in the raw TMDB API JSON response. These will be used for serialization and deserialization.

    static final String JSON_TITLE_KEY = "title";
    static final String JSON_POSTER_URI_KEY = "poster_path";
    static final String JSON_OVERVIEW_KEY = "overview";
    static final String JSON_AVG_RATING_KEY = "vote_average";
    static final String JSON_RELEASE_DATE_KEY = "release_date";
    static final String JSON_RESULT_ARRAY_KEY = "results";

    private static final String TAG = JsonUtils.class.getSimpleName(); // for debugging.
    /**
     *
     * @param rawJsonString The unprocessed json string that is to be parsed into Movie objects.
     * @return an array of Movie model objects that contained the fetched data.
     */
    public static Movie[] processJsonString(String rawJsonString){


        Gson gsonParser = new Gson();
        Movie[] movies = null;

        try {
            JSONObject jsonObject = new JSONObject(rawJsonString);
            JSONArray  results = jsonObject.getJSONArray(JSON_RESULT_ARRAY_KEY);



            //Checking if we got some movies in our json output from the api call.

            if(results.length() != 0) {

                //Parsing all that data into an array with a one-liner. GSON library rules!
                movies = gsonParser.fromJson(String.valueOf(results), Movie[].class);

                return movies;

            }else {
                Log.d(TAG, "Results array does not have any movie data in it - processJsonString() returned null");
            }


        }catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Something wrong with the formatting of the raw json received - json object couldn't be created.");

        }

        return movies;
    }
}
