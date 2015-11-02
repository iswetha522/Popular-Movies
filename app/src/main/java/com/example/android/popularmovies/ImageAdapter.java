package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private Movie[] movies;

    public ImageAdapter(Movie[] movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        String posterPath = movies[position].getPoster_path();
        String imageUrl = "http://image.tmdb.org/t/p/w185" + posterPath;
        if(convertView==null) {
             imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(450, 450));
            Picasso.with(context).load(imageUrl).into(imageView);
        }
        else
        {
             imageView = (ImageView) convertView;
            Picasso.with(context).load(imageUrl).into(imageView);
        }
            return imageView;
    }
}