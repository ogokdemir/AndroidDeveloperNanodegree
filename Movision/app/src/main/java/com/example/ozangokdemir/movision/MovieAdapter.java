package com.example.ozangokdemir.movision;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] mDataSource;

    /*
        Constructor takes in the initial data source of the adapter.
        This data source will be dynamically updatable through a public setter.
     */

    public MovieAdapter(Movie[] dataSource) {

        mDataSource = dataSource;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int rowLayoutId = R.layout.movie_item_layout;
        boolean attachToParentImmediately = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(rowLayoutId, parent, attachToParentImmediately);

        MovieViewHolder holder = new MovieViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //This is where the Picasso magic happens!

        Picasso.with(holder.mMoviePoster.getContext())
                .load(mDataSource[position].getmPosterUri())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(holder.mMoviePoster);


    }

    @Override
    public int getItemCount() {
        return mDataSource.length;
    }


    public void updateAdapterDataSource(Movie[] movies){

        mDataSource = movies;
    }



    class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView mMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMoviePoster = (ImageView) itemView.findViewById(R.id.iw_movie_item);
        }
    }


}
