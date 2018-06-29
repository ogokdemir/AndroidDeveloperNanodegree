package com.example.ozangokdemir.movision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.parceler.Parcels;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView (R.id.iw_detail_activity_poster) ImageView mMoviePosterIw;
    @BindView(R.id.tw_detail_activity_rating) TextView mAvgRatingTw;
    @BindView(R.id.tw_detail_activity_release_date) TextView mReleaseDataTw;
    @BindView(R.id.tw_detail_activity_overview) TextView mOverViewTw;
    @BindView(R.id.tw_detail_activity_title) TextView mTitleTw;

    //Extras that are sent to this activity will be passed and retrieved with this key.
    public static final String MOVIE_INTENT_KEY = "Detail Activity Mail Box";

    private static final String TAG = DetailActivity.class.getSimpleName(); // for debugging purposes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent(); // will never be null.
        Movie movie = Parcels.unwrap(intent.getParcelableExtra(MOVIE_INTENT_KEY));

        displayMovieData(movie);

    }

    /*
        Inflate the UI with data of the selected movie.
     */

    private void displayMovieData(Movie movie){

        // Display the movie poster.
        Picasso.with(this)
                .load(movie.getmPosterUri())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(mMoviePosterIw);

        // Bind data to other text views in the layout.

        mAvgRatingTw.append("\n"+movie.getmAverageRating()+"/10");
        mReleaseDataTw.append("\n"+movie.getmReleaseDate());
        mTitleTw.setText(movie.getmTitle());
        mOverViewTw.setText(movie.getmOverview());

    }

}
