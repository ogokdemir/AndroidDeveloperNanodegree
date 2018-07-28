package com.example.ozangokdemir.movision.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.ozangokdemir.movision.models.Movie;
import java.util.List;

public class MovieViewModel extends ViewModel {


    //Retrieved movies, wrapped in a ViewHolder so they survive lifecycle callbacks in the controller.

    private LiveData<List<Movie>> mMovies;
    private MovieRepository mRepository;
    private String previousCriteria;

    /**
     * I'm instantiating the dependency in the constructor. I know I should employ Dependency Injection here but
     * I simply couldn't figure out how to work with Dagger 2 and got frustrated.
     *
     */

    public MovieViewModel() {

        Log.d("MovieViewModel", "New View Model instance was created");
        mRepository = new MovieRepository();

    }

    public LiveData<List<Movie>> getMovies(String criteria, String apiKey){

        if(!criteria.equals(previousCriteria))
            mMovies = mRepository.fetchMovies(criteria, apiKey);

        previousCriteria = criteria;
        return mMovies;
    }


    public void forceMovieLoading(String criteria, String apiKey){

    }

 }
