package com.example.ozangokdemir.movision.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.example.ozangokdemir.movision.models.Movie;
import com.example.ozangokdemir.movision.models.Review;
import com.example.ozangokdemir.movision.models.Trailer;
import java.util.List;

public class MovieViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();

    //Retrieved movies,trailers and reviews wrapped in a ViewHolder so they survive lifecycle callbacks in the controller.
    private LiveData<List<Movie>> mMovies;
    private LiveData<List<Trailer>> mTrailers;
    private LiveData<List<Review>> mReviews;
    private MovieRepository mRepository;
    private String previousCriteria;

    /**
     * I'm instantiating the dependency in the constructor. I know I should employ Dependency Injection here but
     * I simply couldn't figure out how to work with Dagger 2 and got frustrated.
     *
     */

    public MovieViewModel() {

        Log.d("MovieViewModel", "New View Model instance was created");
        mRepository = MovieRepository.getMovieRepositoryInstance();

    }

    public LiveData<List<Movie>> getMovies(String criteria, String apiKey){

        if(!criteria.equals(previousCriteria))
            mMovies = mRepository.fetchMovies(criteria, apiKey);

        previousCriteria = criteria;
        return mMovies;
    }

    public  LiveData<List<Trailer>> getTrailers(int movieId, String apiKey){

        Log.d(TAG, "getTrailers was called");

        mTrailers = mRepository.fetchTrailers(movieId, apiKey);
        /*
            THIS IS NULL, IT mTrailers doesn't get initialized for some reason.
         */
        Log.d(TAG, String.valueOf(mTrailers == null));

        return mTrailers;
    }

    public LiveData<List<Review>> getReviews(int movieId, String apiKey){

        Log.d(TAG, "getReviews was called.");

        mReviews = mRepository.fetchReviews(movieId, apiKey);

        Log.d(TAG, String.valueOf(mReviews == null));

        return mReviews;


    }

 }
