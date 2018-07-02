package com.example.ozangokdemir.movision;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.parceler.Parcels;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener, OnTaskCompleted{

    private String mChoosenSortParameter;
    @BindView(R.id.rw_grid_container) RecyclerView recyclerView;
    private Movie[] currentMovieList;
    private MovieAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Initial setup.
        mChoosenSortParameter = getString(R.string.sort_by_default);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        //initial data source is null, new data will be bound to the adapter after first fetchMovies() executes.
        adapter = new MovieAdapter(null, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchMovies();

    }


    /**
     * Checks the internet connection-
     if connected, instantiates a new asynctask and executes;
     if not connected, notifies the user and prompts for action.
     */
    private void fetchMovies(){

        if(NetworkUtils.isOnline(this)) {
            FetchMovieDataTask asyncTask = new FetchMovieDataTask(getString(R.string.api_key), this);
            asyncTask.execute(mChoosenSortParameter);

        }else
            notifyUserOfNoInternetConnection();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        new MenuInflater(this).inflate(R.menu.action_menu, menu);
        return true;

    }


    /**
     * @param item Options menu item that was clicked. These items are sorting criteria (popular, top rated etc.)
     */

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


        fetchMovies();

        return super.onOptionsItemSelected(item);
    }



    /**
     * Handling the MovieAdapter's notification on a movie item click.
     *
     * @param movieItemIdx index of the movie poster that was tapped.
     */
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
     * Helper method for notifying the user that their device is not connected to the internet.
     *
     * Displays an alert dialog and prompts the user for an action: reconnect and try again or exit the app.
     *
     */

    private void notifyUserOfNoInternetConnection(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_dialog_title));
        builder.setCancelable(true);

        builder
                .setMessage(getString(R.string.alert_dialog_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_positive_button),new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,int id) {
                        /*
                            User reconnected and wants to try again.
                         */
                        fetchMovies();
                    }
                })
                .setNegativeButton(getString(R.string.alert_dialog_negative_button),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        /*
                            User chose to exit the app.
                         */
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }


    /**
     * Handling FetchMovieDataTask's notification that the task was completed.
     *
     * @param movies The array of movies that was returned by onPostExecute()
     */
    @Override
    public void onTaskCompleted(Movie[] movies) {

        if(movies!= null)
            currentMovieList = movies;


        adapter.updateAdapterDataSource(movies);
        recyclerView.setAdapter(adapter);

    }
}