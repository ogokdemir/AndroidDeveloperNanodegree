package com.example.ozangokdemir.movision.models;

import java.util.List;

public class InitialReviewResponse {

    int id;
    int page;
    List<Review> results;


    public List<Review> getReviewResults() {
        return results;
    }
}
