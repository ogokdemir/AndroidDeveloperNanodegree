package com.example.ozangokdemir.movision;

import android.content.Intent;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rw_grid_container);
        mChoosenSortParameter = getString(R.string.sort_by_default); // default sort criteria is top rated movies.

        //The user of the code will pass their own api key to the FetchMovieDataTask creation.
         asyncTask = new FetchMovieDataTask(getResources().getString(R.string.api_key));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 2);


        try {

            currentMovieList = asyncTask.execute(mChoosenSortParameter).get();
            adapter = new MovieAdapter(currentMovieList,this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
     * @param choosenSortParameter The sorting criteria that the user picks from the drop down action bar menu.
     */
    private void updateCurrentMovieList(String choosenSortParameter){

        try {
            asyncTask = new FetchMovieDataTask(getString(R.string.api_key));
            currentMovieList = asyncTask.execute(choosenSortParameter).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "Asynctask HTTP GET Request was interrupted!");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
}
