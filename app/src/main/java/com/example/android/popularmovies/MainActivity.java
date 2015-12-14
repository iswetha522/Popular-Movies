package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.sort_by_spinner)
    Spinner spinner;
    @Bind(R.id.gridview)
    GridView gridView;

    private Movie[] movies;
    private Context context = this;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), SingleMovieActivity.class);
                Movie movie = (Movie) (((GridView) parent).getAdapter().getItem(position));
                i.putExtra("movie", movie);
                startActivityForResult(i, 1);
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.v("Selected", selectedItem);
                if(selectedItem.equals("favorites")){
                    ArrayList<Movie> favoriteMovies = new ArrayList<Movie>();
                    for(int i = 0; i < movies.length; i++){
                        Movie mv = movies[i];
                        if(mv.isFavorite()){
                            favoriteMovies.add(mv);
                        }
                    }
                    gridView.setAdapter(new ImageAdapter(favoriteMovies.toArray(new Movie[]{}), context));

                }else {
                    refreshMovies(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String sortValue = "popularity";

        Gson gson = new Gson();
        String moviesJson = sharedPref.getString("movies", gson.toJson(new Movie[]{}));
        movies = gson.fromJson(moviesJson, Movie[].class);


        if(movies == null || movies.length == 0){
            refreshMovies(sortValue);
        }else {
            gridView.setAdapter(new ImageAdapter(movies, getApplicationContext()));
        }
    }

    private void refreshMovies(String sortValue) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String requestUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=" + sortValue + ".desc&api_key=[YOUR API KEY]&page=1";
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
                        Log.v("OkHttp", responseStr);
                        Log.v("OkHttp - string.value of response body", String.valueOf(response.body()));
                        Log.v("OkHttp - string.value of response", String.valueOf(response));
                        Gson gson = new Gson();
                        final MovieListResponse movieListResponse = gson.fromJson(responseStr, MovieListResponse.class);

                        movies = movieListResponse.getResults();

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("movies", gson.toJson(movies));
                        editor.commit();


                        Log.v("movieList response -  length ", String.valueOf(movies));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridView.setAdapter(new ImageAdapter(movies, getApplicationContext()));
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Movie movie = (Movie) (data.getExtras().get("movie"));

                for (int i = 0; i < movies.length ; i++) {
                    Movie mv = movies[i];
                    if (mv.getId() == movie.getId()) {
                        movies[i] = movie;
                    }
                }
            }
        }
    }
}