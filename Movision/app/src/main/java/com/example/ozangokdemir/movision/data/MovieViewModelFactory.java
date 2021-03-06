package com.example.ozangokdemir.movision.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final String mSortCriteria;
    private final String mApiKey;

    public MovieViewModelFactory(String sortCriteria, String apiKey) {
        mSortCriteria = sortCriteria;
        mApiKey = apiKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new MovieViewModel();
    }
}
