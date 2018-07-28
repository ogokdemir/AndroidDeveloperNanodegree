package com.example.ozangokdemir.movision.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ozangokdemir.movision.models.Movie;
import com.example.ozangokdemir.movision.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mDataSource;
    private final MovieItemClickListener mMovieItemClickListener;

    /**
     * Parameters of the adapter are passed by the MainActivity upon instantiation of an adapter object.
     *
     * @param dataSource Movie array that the adapter uses as the data source.
     * @param listener   A reference to the MainActivity's implementation of the MovieItemClickListener.
     */

    public MovieAdapter(List<Movie> dataSource, MovieItemClickListener listener) {

        mDataSource = dataSource;
        mMovieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int rowLayoutId = R.layout.movie_item_layout;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(rowLayoutId, parent, false);



        return new MovieViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //This is where the Picasso magic happens! Binding thumbnail poster to movie items in the grid.

        Picasso.with(holder.mMoviePoster.getContext())
                .load(mDataSource.get(position).getPosterUri())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(holder.mMoviePoster);

        holder.mMovieRating.setText(String.valueOf(mDataSource.get(position).getAverageRating()));
        holder.mMovieTitle.setText(mDataSource.get(position).getTitle());

        holder.mMoviePoster.setContentDescription("Picture for " + mDataSource.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mMoviePoster;
        final TextView mMovieTitle;
        final TextView mMovieRating;

        MovieViewHolder(View itemView) {
            super(itemView);

            mMoviePoster = itemView.findViewById(R.id.iw_movie_item);
            mMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            mMovieRating = itemView.findViewById(R.id.tv_movie_rating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedItemPosition = getAdapterPosition();
            mMovieItemClickListener.onMovieItemClick(clickedItemPosition);
        }
    }


    public void updateAdapterDataSource(List<Movie> movies){
        mDataSource = movies;
    }

}
