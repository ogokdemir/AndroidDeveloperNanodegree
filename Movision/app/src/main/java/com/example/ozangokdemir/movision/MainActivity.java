package com.example.ozangokdemir.movision;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.concurrent.ExecutionException;

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



public class MainActivity extends AppCompatActivity {

    private String mChoosenSortParameter; // will be set by the menu call.
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rw_grid_container);

        //The user of the code will pass their own api key to the FetchMovieDataTask creation.

        FetchMovieDataTask task = new FetchMovieDataTask(getResources().getString(R.string.api_key));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 2);


        try {
            MovieAdapter adapter = new MovieAdapter(task.execute(getResources().getString(R.string.sort_by_popular)).get());



            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
