package com.tzolas.filmtracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView title, director, year, genre, rating;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Enlazar vistas con el diseño
        title = findViewById(R.id.detailTitle);
        director = findViewById(R.id.detailDirector);
        year = findViewById(R.id.detailYear);
        genre = findViewById(R.id.detailGenre);
        rating = findViewById(R.id.detailRating);

        movieDatabase = MovieDatabase.getInstance(this);

        // Obtener el ID de la película desde el intent
        int movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails(movieId);
        }
    }

    private void loadMovieDetails(int movieId) {
        new Thread(() -> {
            Movie movie = movieDatabase.movieDao().getMovieById(movieId);
            runOnUiThread(() -> {
                if (movie != null) {
                    title.setText(movie.getTitle());
                    director.setText(movie.getDirector());
                    year.setText(String.valueOf(movie.getYear()));
                    genre.setText(movie.getGenre());
                    rating.setText(String.valueOf(movie.getRating()));
                }
            });
        }).start();
    }
}
