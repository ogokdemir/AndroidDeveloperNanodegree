package com.example.ozangokdemir.movision;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    @GET("movie/{sort_param}")
    Call<InitialMovieResponse> getInitialResponse(@Path("sort_param") String sort_param, @Query("api_key") String apiKey);

}
