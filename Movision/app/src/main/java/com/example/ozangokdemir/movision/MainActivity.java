package com.example.ozangokdemir.movision;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.concurrent.ExecutionException;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mChoosenSortParameter;
    @BindView(R.id.rw_grid_container) RecyclerView recyclerView;
    private FetchMovieDataTask asyncTask;
    private Movie[] currentMovieList;
    private MovieAdapter adapter;
    private GridLayoutManager layoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Initial setup.
        mChoosenSortParameter = getString(R.string.sort_by_default);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        fetchMovies();

    }


    /**
     * Instantiates an asynctask object, executes, passes the fetched data to the adapter.
     */

    public void fetchMovies(){

        /*
            Note to reviewer:

            I know that instantiating a new asynctask on each call is not the best practice here and that I need to use
            an AsyncTaskLoader but I don't know how to use those yet.
         */

        asyncTask = new FetchMovieDataTask(getResources().getString(R.string.api_key));
        currentMovieList = executeAsynctaskWithNetworkCheck(asyncTask);

        if(currentMovieList != null) // if there was connection and some movies could be fetched:
            adapter = new MovieAdapter(currentMovieList, this);
        else
            return;

        recyclerView.setAdapter(adapter);

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

            fetchMovies();

        }else{
            notifyUserOfNoInternetConnection();
            return;
        }

        //Updating the adapter's data source with the freshly updated Movie[] array.
        adapter.updateAdapterDataSource(currentMovieList);

        //setting the adapter of the recyclerview changes what user sees on the screen.
        recyclerView.setAdapter(adapter);



    }

    /**
     *
     * @param task The Asynctask object on which the execute() is being called.
     * @return Movie array that FetchMovieDataTask returns.
     */

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

            // Device doesn't have network connection.
        }
        else
            notifyUserOfNoInternetConnection();

        return movieList;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        new MenuInflater(this).inflate(R.menu.action_menu, menu);
        return true;

    }


    /**
     *
     * @param item Options menu item that was clicked. These items are sorting criteria (popular, top rated etc.)
     *
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


        updateCurrentMovieList(mChoosenSortParameter);
        return super.onOptionsItemSelected(item);
    }



    /**
     * Implementation of the listener for the movie poster clicks.
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
                            User choose to exit the app.
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
     * This method checks the network connection of the device. Source: stackoverflow.com
     *
     * @return whether the device has connection or not.
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

