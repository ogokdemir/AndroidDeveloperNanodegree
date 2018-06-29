package com.example.ozangokdemir.movision;



import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * String parameter sets the sort type - popular, top_rated, upcoming or now_playing.
 */

class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]>{


    private static final String TAG = FetchMovieDataTask.class.getSimpleName(); // for debugging purposes.

    private final String mApiKey; //set in the constructor, used in the formApiCall()

    private Movie[] movies;


    /**
     *@param apiKey Constructor takes in the apiKey of the user who is running this code. If this app was to be deployed,
     *              user's api key would be prompted for and passed into this constructor.
     */

    FetchMovieDataTask(String apiKey) {

        mApiKey = apiKey;
    }


 /*
        Note to the reviewer: I know this doInBackground() is huge and messy. I know that
        I should have moved most of this code to a Utility class. Please bare with me here...
  */

    @Override
    protected Movie[] doInBackground(String... params) {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResultString = "";
        StringBuilder pile = new StringBuilder();

        try {

            //Receive the api call url that was formed with the sort criteria input from the user.
            URL searchUrl = formApiCall(params[0]);
            //URL searchUrl = new URL("https://api.themoviedb.org/3/movie/latest?api_key=c4549f30f17241f3794cb1114c2f80ed");

            Log.d(TAG, "Search Url: " + searchUrl);



            //Establish HTTP Connection.

            urlConnection = (HttpURLConnection)searchUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();




            //Getting the input stream - the channel of information flow between the HTTP server and the phone.
            InputStream ioStream = urlConnection.getInputStream();

            //Guardian for checking whether the input stream was established properly.
            if(ioStream == null){
                Log.d(TAG, "InputStream couldn't be established.");
                return null;

            }

            reader = new BufferedReader(new InputStreamReader(ioStream));


            // Pull the data line by line, stack it up in the StringBuilder.

            String line;
            while((line = reader.readLine())!= null ){
                pile.append(line);
            }


            //Check whether any data was pulled or not. If nothing, return.

            if(pile.length() == 0){
                Log.d(TAG,"No data was found in the url address");
                return null;
            }


            // The result was fetched, now it's the time to process the raw string.

            jsonResultString = pile.toString();

            Log.i(TAG,  jsonResultString);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "HTTP URL connection building failed - There's a server error");
        }

        //Network clean-up - return the buffered reader and http connection resources (if) allocated to this process.
        finally {

            if(urlConnection != null)
                urlConnection.disconnect();

            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error closing the BufferedReader instance: reader, check the input stream");
                }
            }
        }


        //Parse the raw json string, return an array of Movie objects as the data source for UI.

        movies = JsonUtils.processJsonString(jsonResultString);

        // Finally return the array of model Movie objects.

        return movies;

    }


    /**
     *
     * @param sortParam The sorting criteria, FetchMovieDataTask takes in this string and passed it into this method.
     *
     * @return The api call URL that will be queried for in the doInBackground().
     */

    private URL formApiCall(String sortParam){


        final String BASE_URL = "https://api.themoviedb.org/3/movie/"+sortParam;
        final String PARAM_API_KEY = "api_key";
        URL searchUrl = null; // will be used in doInBackGround() method.

        /*
            Build the URL based on the user's sorting criteria.
         */

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, mApiKey)
                .build();

        Log.d(TAG, builtUri.toString());

        try {
            searchUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "URL formation failed in formApiCall()");

        }

        return searchUrl;
    }

}