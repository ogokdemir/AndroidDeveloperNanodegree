package com.example.ozangokdemir.movision;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;




public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] mDataSource;
    private final MovieItemClickListener mMovieItemClickListener;

    /**
     * Parameters of the adapter are passed by the MainActivity upon instantiation of an adapter object.
     *
     * @param dataSource Movie array that the adapter uses as the data source.
     * @param listener   A reference to the MainActivity's implementation of the MovieItemClickListener.
     */

    public MovieAdapter(Movie[] dataSource, MovieItemClickListener listener) {

        mDataSource = dataSource;
        mMovieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int rowLayoutId = R.layout.movie_item_layout;
        boolean attachToParentImmediately = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(rowLayoutId, parent, attachToParentImmediately);



        return new MovieViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //This is where the Picasso magic happens! Binding thumbnail poster to movie items in the grid.

        Picasso.with(holder.mMoviePoster.getContext())
                .load(mDataSource[position].getmPosterUri())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(holder.mMoviePoster);

        holder.mMoviePoster.setContentDescription("Picture for " +mDataSource[position].getmTitle());
    }

    @Override
    public int getItemCount() {
        return mDataSource.length;
    }

    /**
     * Allows for passing new movie data dynamically to the existing adapter object.

     * @param movies data source update, new movies.
     */

    public void updateAdapterDataSource(Movie[] movies){

        mDataSource = movies;
    }



    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mMoviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);

            mMoviePoster = itemView.findViewById(R.id.iw_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedItemPosition = getAdapterPosition();
            mMovieItemClickListener.onMovieItemClick(clickedItemPosition);
        }
    }


}
