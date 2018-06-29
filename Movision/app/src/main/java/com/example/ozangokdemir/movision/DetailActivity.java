package com.example.ozangokdemir.movision;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {


    //Extras that are sent to this activity will be passed and retrieved with this key.
    public static final String MOVIE_INTENT_KEY = "Detail Activity Mail Box";

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



    }
}
