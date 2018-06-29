package com.example.ozangokdemir.movision;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mChoosenSortParameter;
    private RecyclerView recyclerView;
    private FetchMovieDataTask asyncTask;
    private Movie[] currentMovieList;
    private MovieAdapter adapter;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rw_grid_container);
        mChoosenSortParameter = getString(R.string.sort_by_default); // default sort criteria is top rated movies.


         asyncTask = new FetchMovieDataTask(getResources().getString(R.string.api_key));

         gridLayoutManager = new GridLayoutManager(this, 2);


      //  try {

            currentMovieList = executeAsynctaskWithNetworkCheck(asyncTask);
            adapter = new MovieAdapter(currentMovieList,this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);

            /*
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        new MenuInflater(this).inflate(R.menu.action_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItemId = item.getItemId();

        switch (selectedItemId){

            case R.id.action_sort_by_popularity:

                mChoosenSortParameter = getString(R.string.sort_by_popular);
                setTitle(getString(R.string.menu_title_popular));
                break;

            case R.id.action_sort_by_top_rated:
                mChoosenSortParameter = getString(R.string.sort_by_top_rated);
                setTitle(getString(R.string.menu_title_top_rated));
                break;

            case R.id.action_sort_by_now_playing:
                mChoosenSortParameter = getString(R.string.sort_by_now_playing);
                setTitle(getString(R.string.menu_title_now_playing));
                break;

            case R.id.action_sort_by_upcoming:
                mChoosenSortParameter = getString(R.string.sort_by_upcoming);
                setTitle(getString(R.string.menu_title_upcoming));
                break;
        }


        updateCurrentMovieList(mChoosenSortParameter);
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method updates the current sorting criteria by instantiating a new asynctasks object and making a new api call.
     *
     * Called when the user picks a different sorting from the option menu.
     *
     * @param choosenSortParameter The sorting criteria that the user picks from the drop down action bar menu.
     */
    private void updateCurrentMovieList(String choosenSortParameter){

        if(isOnline()) {
            setContentView(R.layout.activity_main);


            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

            asyncTask = new FetchMovieDataTask(getString(R.string.api_key));

            recyclerView = (RecyclerView) findViewById(R.id.rw_grid_container);
            recyclerView.setLayoutManager(layoutManager);
            currentMovieList = executeAsynctaskWithNetworkCheck(asyncTask);

            adapter = new MovieAdapter(currentMovieList, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);

            //try {



        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.no_network_connection_error), Toast.LENGTH_LONG)
                    .show();

            setContentView(R.layout.error_layout);
        }
        /*
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "Asynctask HTTP GET Request was interrupted!");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/
        //Updating the adapter's data source with the freshly updated Movie[] array.
        adapter.updateAdapterDataSource(currentMovieList);

        //setting the adapter of the recyclerview changes what user sees on the screen.
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onMovieItemClick(int movieItemIdx) {

        Parcelable wrappedMovie = Parcels.wrap(currentMovieList[movieItemIdx]);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailActivity.MOVIE_INTENT_KEY, wrappedMovie);
        Intent toDetailActivity = new Intent(this, DetailActivity.class);
        toDetailActivity.putExtras(bundle);
        startActivity(toDetailActivity);

        }

    /**
     * This elegant piece of code checks the network connectivity. Source: stackoverflow.com
     *
     * @return State of connectivity - whether the device has network connection.
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Movie[] executeAsynctaskWithNetworkCheck(FetchMovieDataTask task){

        Movie[] movieList = null;

        if(isOnline()) {
            try {
                movieList = task.execute(mChoosenSortParameter).get();

            }
            catch (InterruptedException e) {
                e.printStackTrace();


            } catch (ExecutionException e) {
                e.printStackTrace();

            }
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.no_network_connection_error), Toast.LENGTH_LONG)
                    .show();

            setContentView(R.layout.error_layout);
        }

        return movieList;
    }

/*
    private boolean isCurrentlyOnTheErrorScreen(){

        boolean this.getWindow().getDecorView().findViewById(android.R.id.content).equals(R.layout.activity_main);

    }
*/
}
