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
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener, Callback<InitialMovieResponse>{

    private String mChoosenSortParameter;
    @BindView(R.id.rw_grid_container) RecyclerView recyclerView;
    private List<Movie> currentMovieList; // Cache for the current data source. Consider for ViewModel
    private MovieAdapter adapter;
    Retrofit retrofit;
    RetrofitApiInterface apiInterface;

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


        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(RetrofitApiInterface.class);

        fetchMovies(mChoosenSortParameter);

    }



    /**
     * !!!!! NOTE !!!!
     *
     * 1)Retrofit calls are currently maid on the UI thread, look into rxjava for pushing fetch to background thread.
     * 2)When 1 is accomplished, delete the NetworkUtils class.
     *
     * Checks the internet connection-
     if connected, instantiates a new asynctask and executes;
     if not connected, notifies the user and prompts for action.
     */
    private void fetchMovies(String choosenParameter){

        if(NetworkUtils.isOnline(this)) {
            Call<InitialMovieResponse> call = apiInterface.getInitialResponse(mChoosenSortParameter,
                    getString(R.string.api_key));

            call.enqueue(this);

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

        setTitle(item.getTitle());

        switch (item.getItemId()){
            case R.id.action_sort_by_popularity:
                mChoosenSortParameter = getString(R.string.sort_by_popular);
                break;

            case R.id.action_sort_by_top_rated:
                mChoosenSortParameter = getString(R.string.sort_by_top_rated);
                break;

            case R.id.action_sort_by_now_playing:
                mChoosenSortParameter = getString(R.string.sort_by_now_playing);
                break;

            case R.id.action_sort_by_upcoming:
                mChoosenSortParameter = getString(R.string.sort_by_upcoming);
                break;
     }


        fetchMovies(mChoosenSortParameter);
        return super.onOptionsItemSelected(item);
     }



     /**
     * Handling the MovieAdapter's notification on a movie item click.
     *
     * @param movieItemIdx index of the movie poster that was tapped.
     */
    @Override
    public void onMovieItemClick(int movieItemIdx) {

        Parcelable wrappedMovie = Parcels.wrap(currentMovieList.get(movieItemIdx));
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
     *
     * !!! NOTE !!!
     * This is extremely ugly here in the MainActivity, how do I move it outside? Maybe a PopUp activity which will
     * be called from the NetworkUtils?
     *
     *
     *
     */

    private void notifyUserOfNoInternetConnection(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_dialog_title));
        builder.setCancelable(true);

        builder
                .setMessage(getString(R.string.alert_dialog_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alert_dialog_positive_button),new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,int id) {
                        /*
                            User reconnected and wants to try again.
                         */
                        fetchMovies(mChoosenSortParameter);
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
        !!! NOTE !!!

        When I figure out the ViewModel, I'll carry this code into ViewModel class.

        LiveData<List<Movie>> will be cached in this ViewModel so the same data won't be fetched each time.

     */

    @Override
    public void onResponse(Call<InitialMovieResponse> call, Response<InitialMovieResponse> response) {

        List<Movie> movies = response.body().getResults();

        if(movies!= null)
            currentMovieList = movies;

        adapter.updateAdapterDataSource(movies);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Call<InitialMovieResponse> call, Throwable t) {

    }
}