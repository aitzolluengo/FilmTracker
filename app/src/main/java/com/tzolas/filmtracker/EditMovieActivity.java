package com.tzolas.filmtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;

public class EditMovieActivity extends AppCompatActivity {

    // UI Components
    private EditText titleInput, directorInput, yearInput, genreInput, ratingInput;
    private Button saveButton;
    private MovieDatabase movieDatabase;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        // Enlazar vistas con el diseño
        titleInput = findViewById(R.id.inputTitle);
        directorInput = findViewById(R.id.inputDirector);
        yearInput = findViewById(R.id.inputYear);
        genreInput = findViewById(R.id.inputGenre);
        ratingInput = findViewById(R.id.inputRating);
        saveButton = findViewById(R.id.btnSaveMovie);

        // Obtener instancia de la base de datos
        movieDatabase = MovieDatabase.getInstance(this);

        // Obtener el ID de la película desde el intent
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails(movieId);
        }

        // Configurar botón para guardar cambios
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMovie();
            }
        });
    }

    // Método para cargar los detalles de la película
    private void loadMovieDetails(int movieId) {
        new Thread(() -> {
            Movie movie = movieDatabase.movieDao().getMovieById(movieId);
            runOnUiThread(() -> {
                if (movie != null) {
                    titleInput.setText(movie.getTitle());
                    directorInput.setText(movie.getDirector());
                    yearInput.setText(String.valueOf(movie.getYear()));
                    genreInput.setText(movie.getGenre());
                    ratingInput.setText(String.valueOf(movie.getRating()));
                }
            });
        }).start();
    }

    // Método para actualizar la película en la base de datos
    private void updateMovie() {
        String title = titleInput.getText().toString().trim();
        String director = directorInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        String genre = genreInput.getText().toString().trim();
        String ratingStr = ratingInput.getText().toString().trim();

        // Validar que todos los campos están completos
        if (title.isEmpty() || director.isEmpty() || yearStr.isEmpty() || genre.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = Integer.parseInt(yearStr);
        float rating = Float.parseFloat(ratingStr);
        Movie updatedMovie = new Movie(title, director, year, genre, rating);
        updatedMovie.setId(movieId);

        new Thread(() -> {
            movieDatabase.movieDao().updateMovie(updatedMovie);
            runOnUiThread(() -> {
                Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Notificar que se realizó una actualización
                finish(); // Cerrar la actividad
            });
        }).start();
    }
}
