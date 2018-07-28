package com.example.ozangokdemir.movision.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.example.ozangokdemir.movision.InitialMovieResponse;
import com.example.ozangokdemir.movision.Movie;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    RetrofitApiInterface mWebservice;


    /**
     * I'm instantiating the dependency in the constructor. I know I should employ Dependency Injection here but
     * I simply couldn't figure out how to work with Dagger 2 and got frustrated.
     */
    public MovieRepository() {
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

            MutableLiveData<List<Movie>> data = new MutableLiveData<>();

            mWebservice.getInitialResponse(criteria, apiKey).enqueue(new Callback<InitialMovieResponse>() {

                @Override
                public void onResponse(Call<InitialMovieResponse> call, Response<InitialMovieResponse> response) {

                    Log.d(TAG, "Data was fetched from the web" );
                    data.setValue(response.body().getResults());
                }

                @Override
                public void onFailure(Call<InitialMovieResponse> call, Throwable t) {

                    Log.d(TAG, "Retrofit TMDB Api call failed in MovieRepository");
                }
            });

            return data;
    }
}
