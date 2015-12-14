package com.example.android.popularmovies;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleMovieActivity extends Activity {

    @Bind(R.id.original_title_text_view) TextView originalTitle;
    @Bind(R.id.movie_poster_image_view) ImageView imageView;
    @Bind(R.id.synopsis_text_view) TextView synopsis;
    @Bind(R.id.user_rating_text_view) TextView userRating;
    @Bind(R.id.release_date_text_view) TextView releaseDate;
    @Bind(R.id.review_list_view) ListView listView;
    @Bind(R.id.video_button) Button button;
    @Bind(R.id.toggle_button) ToggleButton toggleButton;
    private Context context = this;
    private Movie movie;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie_details);
        ButterKnife.bind(this);

        Intent i = getIntent();
        this.movie = (Movie) (i.getExtras().get("movie"));

        originalTitle.setText(movie.getOriginal_title());

        Picasso.with(this).load("http://image.tmdb.org/t/p/w780" + movie.getBackdrop_path()).into(imageView);

        synopsis.setText(movie.getOverview());

        String average = Double.toString(movie.getVote_average());
        userRating.setText(average);

        releaseDate.setText(movie.getRelease_date());

        fetchReview(movie.getId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTrailer(movie.getId());
            }
        });

        toggleButton.setChecked(movie.isFavorite());

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(toggleButton.isChecked()){
                    movie.setFavorite(true);
                } else {
                    movie.setFavorite(false);
                }
            }
        });
    }

    private void fetchReview(int id){
        OkHttpClient okHttpClient = new OkHttpClient();
        String requestUrl = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=[YOUR API KEY]";
        Request request = new Request.Builder().url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        Gson gson = new Gson();
                        final ReviewsListResponse reviewsListResponse = gson.fromJson(responseStr, ReviewsListResponse.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(new ArrayAdapter<Review>(context, R.layout.list_row, reviewsListResponse.getResults()));
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTrailer(int id){
        OkHttpClient okHttpClient = new OkHttpClient();
        String requestUrl = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=[YOUR API KEY]";
        Request request = new Request.Builder().url(requestUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    if(response.isSuccessful()){
                        String responseStr = response.body().string();
                        Log.v("OkHttp", responseStr);
                        Log.v("OkHttp - string.value of response body", String.valueOf(response.body()));
                        Log.v("OkHttp - string.value of response", String.valueOf(response));
                        Gson gson = new Gson();
                        final TrailersListResponse trailersListResponse = gson.fromJson(responseStr,TrailersListResponse.class);
                        Log.v("movieList response -  length ", String.valueOf(trailersListResponse.getResults()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String key = trailersListResponse.getResults()[0].getKey();
                                watchYoutubeVideo(key);
                            }
                        });
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void watchYoutubeVideo(String videoKey){

        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" +videoKey));
            startActivity(intent);
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("movie", movie);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
