package com.example.ozangokdemir.movision.models;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InitialTrailerResponse {

    @SerializedName("id")
    private int mId;
    @SerializedName("results")
    private List<Trailer> mTrailers;

    public List<Trailer> getTrailerResults() {
        return  mTrailers;
    }

}
