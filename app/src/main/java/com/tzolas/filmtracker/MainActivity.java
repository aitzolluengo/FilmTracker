package com.tzolas.filmtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieDatabase = MovieDatabase.getInstance(this);
        loadMovies();

        findViewById(R.id.btnAddMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMovieActivity.class));
            }
        });
    }

    private void loadMovies() {
        new Thread(() -> {
            List<Movie> movieList = movieDatabase.movieDao().getAllMovies();
            runOnUiThread(() -> {
                movieAdapter = new MovieAdapter(movieList, MainActivity.this);
                recyclerView.setAdapter(movieAdapter);
            });
        }).start();
    }
}
