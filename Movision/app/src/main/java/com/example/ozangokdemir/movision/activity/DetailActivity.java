package com.example.ozangokdemir.movision.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ozangokdemir.movision.models.Movie;
import com.example.ozangokdemir.movision.R;
import com.example.ozangokdemir.movision.models.Trailer;
import com.squareup.picasso.Picasso;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView (R.id.iw_detail_activity_poster)ImageView mMoviePosterIw;
    @BindView(R.id.tw_detail_activity_rating) TextView mAvgRatingTw;
    @BindView(R.id.tw_detail_activity_release_date) TextView mReleaseDataTw;
    @BindView(R.id.tw_detail_activity_overview) TextView mOverViewTw;
    @BindView(R.id.tw_detail_activity_title) TextView mTitleTw;

    //Extras that are sent to this activity will be passed and retrieved with this key.
    public static final String MOVIE_INTENT_KEY = "Detail Activity Movie Mail Box";
    public static final String TRAILERS_INTENT_KEY = "Detail Activity Trailers Mail Box";
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent(); // will never be null.
        Movie movie = Parcels.unwrap(intent.getParcelableExtra(MOVIE_INTENT_KEY));
        List<Trailer> trailers = Parcels.unwrap(intent.getParcelableExtra(TRAILERS_INTENT_KEY));

        for(Trailer t : trailers){

            Log.d(TAG, t.getName()+"\n" + t.getType() + "\n" + t.getKey() +"\n" + t.getSite()+ "\n");
        }

        displayMovieData(movie);
    }

    /*
        Helper method for inflating the UI with data of the selected movie.
     */

    private void displayMovieData(Movie movie){

        // Display the movie poster.
        Picasso.with(this)
                .load(movie.getPosterUri())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(mMoviePosterIw);


        // Bind data to other text views in the layout.
        mAvgRatingTw.append("\n"+movie.getAverageRating()+"/10");
        mReleaseDataTw.append("\n"+movie.getReleaseDate());
        mTitleTw.setText(movie.getTitle());
        mOverViewTw.setText(movie.getOverview());
        mMoviePosterIw.setContentDescription(getString(R.string.image_content_decription)+movie.getTitle());

    }
}
