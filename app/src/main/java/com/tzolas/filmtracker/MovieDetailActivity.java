package com.tzolas.filmtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    // UI Components
    private TextView title, director, year, genre, rating;
    private Button btnEdit, btnDelete;
    private MovieDatabase movieDatabase;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Inicializar vistas
        title = findViewById(R.id.detailTitle);
        director = findViewById(R.id.detailDirector);
        year = findViewById(R.id.detailYear);
        genre = findViewById(R.id.detailGenre);
        rating = findViewById(R.id.detailRating);
        btnEdit = findViewById(R.id.btnEditMovie);
        btnDelete = findViewById(R.id.btnDeleteMovie);

        // Obtener instancia de la base de datos
        movieDatabase = MovieDatabase.getInstance(this);

        // Obtener el ID de la película desde el intent
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails(movieId); // Cargar detalles de la película
        }

        // Botón para editar la película
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditMovieActivity.class);
            intent.putExtra("MOVIE_ID", movieId);
            startActivityForResult(intent, 1); // Esperar resultado de edición
        });

        // Botón para eliminar la película con confirmación
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    // Cargar detalles de la película desde la base de datos
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

    // Confirmar eliminación de la película
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Película")
                .setMessage("¿Estás seguro de que quieres eliminar esta película?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMovie();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Eliminar la película de la base de datos y actualizar MainActivity
    private void deleteMovie() {
        new Thread(() -> {
            Movie movie = movieDatabase.movieDao().getMovieById(movieId);
            if (movie != null) {
                movieDatabase.movieDao().deleteMovie(movie);
                runOnUiThread(() -> {
                    setResult(RESULT_OK, new Intent()); // Notificar a MainActivity
                    finish(); // Cierra la actividad y vuelve a MainActivity
                });
            }
        }).start();
    }

    // Manejar el resultado de la edición de la película
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadMovieDetails(movieId); // Recargar detalles después de editar
            setResult(RESULT_OK, new Intent()); // Notificar a MainActivity
        }
    }
}
