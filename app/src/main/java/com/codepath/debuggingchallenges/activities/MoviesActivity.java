package com.codepath.debuggingchallenges.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.debuggingchallenges.R;
import com.codepath.debuggingchallenges.adapters.MoviesAdapter;
import com.codepath.debuggingchallenges.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class MoviesActivity extends AppCompatActivity {

    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    RecyclerView rvMovies;
    MoviesAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        rvMovies = findViewById(R.id.rvMovies);

        movies = new ArrayList<Movie>();
        // Create the adapter to convert the array to views
        adapter = new MoviesAdapter(MoviesActivity.this, movies);

        // Attach the adapter to a ListView
        rvMovies.setAdapter(adapter);

        rvMovies.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));
        fetchMovies();
    }


    private void fetchMovies() {
        String url = " https://api.themoviedb.org/3/movie/now_playing?api_key=a71c0acdb84c7fdef6c324846d8ec1f1";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                JSONObject jsonObject = response.jsonObject;
                try {
                    JSONArray moviesJson = jsonObject.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(moviesJson));
                    Log.d("MoviesActivityDebug", "number movies adapter: " + adapter.getItemCount());
                    adapter.notifyDataSetChanged();
                    Log.d("MoviesActivityDebug", "movies size: " + movies.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(MoviesActivity.class.getSimpleName(), "Error retrieving movies: ", throwable);
            }
        });
    }
}
