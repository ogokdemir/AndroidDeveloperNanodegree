package com.example.ozangokdemir.movision.activity;

import android.arch.lifecycle.ViewModelProviders;
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
import com.example.ozangokdemir.movision.models.Movie;
import com.example.ozangokdemir.movision.utils.*;
import com.example.ozangokdemir.movision.R;
import com.example.ozangokdemir.movision.adapter.MovieAdapter;
import com.example.ozangokdemir.movision.adapter.MovieItemClickListener;
import com.example.ozangokdemir.movision.data.MovieViewModel;
import org.parceler.Parcels;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mChosenSortParameter;
    @BindView(R.id.rw_grid_container) RecyclerView recyclerView;
    private com.example.ozangokdemir.movision.adapter.MovieAdapter adapter;
    private MovieViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Setting up the RecyclerView, it's data source will be set in the setupViewModel method.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new MovieAdapter(null, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        if(savedInstanceState != null && savedInstanceState.getString(TAG)!= null)
            mChosenSortParameter = savedInstanceState.getString(TAG);
        else
        mChosenSortParameter = getString(R.string.sort_by_default);

        setupViewModel(mChosenSortParameter);
    }



    private void setupViewModel(String chosenParameter){

        if(NetworkUtils.isOnline(this)) {

            viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

            viewModel.getMovies(chosenParameter, getString(R.string.api_key)).observe(this, movies -> {

                adapter.updateAdapterDataSource(movies);

                recyclerView.setAdapter(adapter);
            });
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
                mChosenSortParameter = getString(R.string.sort_by_popular);
                break;

            case R.id.action_sort_by_top_rated:
                mChosenSortParameter = getString(R.string.sort_by_top_rated);
                break;

            case R.id.action_sort_by_now_playing:
                mChosenSortParameter = getString(R.string.sort_by_now_playing);
                break;

            case R.id.action_sort_by_upcoming:
                mChosenSortParameter = getString(R.string.sort_by_upcoming);
                break;

        }

        setupViewModel(mChosenSortParameter);
        return super.onOptionsItemSelected(item);
    }


    /**
     * Handling the MovieAdapter's notification on a movie item click.
     *
     * @param movieItemIdx index of the movie poster that was tapped.
     */
    @Override
    public void onMovieItemClick(int movieItemIdx) {

        Intent toDetailActivity = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();

        //It's OK to make a new call to the viewModel because it caches the result until the mChosenSortParameter changes.
        List<Movie> displayedMovies = viewModel.getMovies(mChosenSortParameter, getString(R.string.api_key)).getValue();

        //Parcel the movie object that was tapped by the user, to send it to the DetailActivity.
        Parcelable wrappedMovie = Parcels.wrap(displayedMovies.get(movieItemIdx));

        bundle.putParcelable(DetailActivity.MOVIE_INTENT_KEY, wrappedMovie);


        //Fetch the trailers for the clicked movie using it's id, Parcel the result, and pass it over to the DetailActivity.
        //When the trailers are loaded for the chosen movie, the DetailActivity gets started.
        viewModel.getTrailers(displayedMovies.get(movieItemIdx).getId(), getString(R.string.api_key)).observe(this, trailers -> {

            Parcelable wrappedTrailers = Parcels.wrap(trailers);
            bundle.putParcelable(DetailActivity.TRAILERS_INTENT_KEY, wrappedTrailers);


            viewModel.getReviews(displayedMovies.get(movieItemIdx).getId(), getString(R.string.api_key)).observe(this, reviews ->{
                Parcelable wrappedReviews = Parcels.wrap(reviews);
                bundle.putParcelable(DetailActivity.REVIEWS_INTENT_KEY, wrappedReviews);

                toDetailActivity.putExtras(bundle);
                startActivity(toDetailActivity);
            });
        });


    }


    /**
     * Helper method for notifying the user that their device is not connected to the internet.
     **/

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
                        setupViewModel(mChosenSortParameter);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG, mChosenSortParameter);
    }
}