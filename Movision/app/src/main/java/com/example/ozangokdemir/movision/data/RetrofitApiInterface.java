package com.example.ozangokdemir.movision.data;

import com.example.ozangokdemir.movision.models.InitialMovieResponse;
import com.example.ozangokdemir.movision.models.InitialReviewResponse;
import com.example.ozangokdemir.movision.models.InitialTrailerResponse;
import com.example.ozangokdemir.movision.models.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    String BASE_URL = "http://api.themoviedb.org/3/";


    @GET("movie/{sort_param}")
    Call<InitialMovieResponse> getInitialResponse(@Path("sort_param") String sort_param, @Query("api_key") String apiKey);


    @GET("movie/{movie_id}/videos")
    Call<InitialTrailerResponse> getTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<InitialReviewResponse> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

}
