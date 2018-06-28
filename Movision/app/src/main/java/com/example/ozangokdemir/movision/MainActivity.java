package com.example.ozangokdemir.movision;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
Recycler view implementation
1-Add RecyclerView support library to the gradle build file (DONE)
2-Define a model class to use as the data source (DONE)
3-Add a RecyclerView to your activity to display the items (DONE)
4-Create a custom row layout XML file to visualize the item (DONE) //Only an imageview for the MainActivity.
5-Create a RecyclerView.Adapter and ViewHolder to render the item
6-Bind the adapter to the data source to populate the RecyclerView
 */



//TODO Write an asynctask and fetch the movie data.

public class MainActivity extends AppCompatActivity {


    private String mChoosenSortParameter; // will be set by the menu call.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The user of the code will pass their own api key to the FetchMovieDataTask creation.

        FetchMovieDataTask task = new FetchMovieDataTask("INSERT API KEY HERE!");

        //The onClickMenuItem method will set the mChoosenSortParameter method, which will update the parameter of execute().

        task.execute(getString(R.string.sort_by_popular));
    }
}
