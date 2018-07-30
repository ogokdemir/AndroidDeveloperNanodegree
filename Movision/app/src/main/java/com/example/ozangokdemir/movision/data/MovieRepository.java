package com.example.ozangokdemir.movision.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.widget.TableRow;

import com.example.ozangokdemir.movision.models.InitialMovieResponse;
import com.example.ozangokdemir.movision.models.InitialTrailerResponse;
import com.example.ozangokdemir.movision.models.Movie;
import com.example.ozangokdemir.movision.models.Trailer;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class contains and centralizes all the code for making the calls to fetch the data.
 * This class is a Singleton.
 */

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();
    private static final MovieRepository mSingletonRepository = new MovieRepository();

    RetrofitApiInterface mWebservice;


    /**
     * I'm instantiating the dependency in the constructor. I know I should employ Dependency Injection here but
     * I simply couldn't figure out how to work with Dagger 2 and got frustrated.
     */
    private MovieRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mWebservice = retrofit.create(RetrofitApiInterface.class);

        Log.d(TAG,"New Repository Created");
    }


    //LiveData works in the background thread by default so this task is asynchronous.

    public LiveData<List<Movie>> fetchMovies(String criteria, String apiKey) {

            Log.d(TAG, "fetchMovies was called");

            MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

            mWebservice.getInitialResponse(criteria, apiKey).enqueue(new Callback<InitialMovieResponse>() {

                @Override
                public void onResponse(Call<InitialMovieResponse> call, Response<InitialMovieResponse> response) {

                    Log.d(TAG, "Data was fetched from the web" );
                    movies.postValue(response.body().getMovieResults());
                }

                @Override
                public void onFailure(Call<InitialMovieResponse> call, Throwable t) {

                    Log.d(TAG, "Retrofit TMDB Api call failed in MovieRepository");
                }
            });

            return movies;
    }


    public LiveData<List<Trailer>> fetchTrailers(int movieId, String apiKey){

        MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();

        Log.d(TAG, "fetch trailers was called");

        mWebservice.getTrailers(movieId, apiKey).enqueue(new Callback<InitialTrailerResponse>() {
            @Override
            public void onResponse(Call<InitialTrailerResponse> call, Response<InitialTrailerResponse> response) {

                Log.d(TAG, "Trailer was fetched for movie");


                /*
                    THE PROBLEM IS FUCKING HERE!

                 */
                trailers.postValue(response.body().getTrailerResults());
                Log.d(TAG, String.valueOf(trailers.getValue() == null));


            }


            @Override
            public void onFailure(Call<InitialTrailerResponse> call, Throwable t) {

                Log.d(TAG, "Trailer fetching failed in the MovieRepository");

            }
        });

        Log.d(TAG, String.valueOf(trailers.getValue() == null));
        return trailers;

    }

    public static MovieRepository getMovieRepositoryInstance() {

        return mSingletonRepository;
    }
}
