package com.example.android.popularmovies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SingleViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_page);

        Intent i = getIntent();
        Movie movie = (Movie) (i.getExtras().get("movie"));

        TextView originalTitle = (TextView) findViewById(R.id.original_title_text_view);
        originalTitle.setText(movie.getOriginal_title());

        ImageView imageView = (ImageView) findViewById(R.id.movie_poster_image_view);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780" + movie.getBackdrop_path()).into(imageView);

        TextView synopsis  = (TextView) findViewById(R.id.synopsis_text_view);
        synopsis.setText(movie.getOverview());

        TextView userRating = (TextView) findViewById(R.id.user_rating_text_view);
        String average = new Double(movie.getVote_average()).toString();
        userRating.setText(average);

        TextView releaseDate = (TextView) findViewById(R.id.release_date_text_view);
        releaseDate.setText(movie.getRelease_date());








    }

}
