package com.tzolas.filmtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;

public class AddMovieActivity extends AppCompatActivity {

    private EditText titleInput, directorInput, yearInput, genreInput, ratingInput;
    private Button saveButton;
    private MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        // Enlazar vistas con el diseño
        titleInput = findViewById(R.id.inputTitle);
        directorInput = findViewById(R.id.inputDirector);
        yearInput = findViewById(R.id.inputYear);
        genreInput = findViewById(R.id.inputGenre);
        ratingInput = findViewById(R.id.inputRating);
        saveButton = findViewById(R.id.btnSaveMovie);

        movieDatabase = MovieDatabase.getInstance(this);

        // Configurar botón para guardar película
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovie();
            }
        });
    }

    private void saveMovie() {
        String title = titleInput.getText().toString().trim();
        String director = directorInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        String genre = genreInput.getText().toString().trim();
        String ratingStr = ratingInput.getText().toString().trim();

        if (title.isEmpty() || director.isEmpty() || yearStr.isEmpty() || genre.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = Integer.parseInt(yearStr);
        float rating = Float.parseFloat(ratingStr);
        Movie movie = new Movie(title, director, year, genre, rating);

        new Thread(() -> {
            movieDatabase.movieDao().insertMovie(movie);
            runOnUiThread(() -> {
                Toast.makeText(this, "Película guardada", Toast.LENGTH_SHORT).show();
                finish(); // Cerrar la actividad
            });
        }).start();
    }
}
