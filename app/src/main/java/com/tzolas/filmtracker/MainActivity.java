package com.tzolas.filmtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
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

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicialización de la base de datos
        movieDatabase = MovieDatabase.getInstance(this);
        loadMovies(); // Cargar películas al iniciar la actividad

        // Botón para añadir nuevas películas
        findViewById(R.id.btnAddMovie).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    // Método para cargar películas desde la base de datos y mostrarlas en la lista
    private void loadMovies() {
        new Thread(() -> {
            List<Movie> movieList = movieDatabase.movieDao().getAllMovies();
            runOnUiThread(() -> {
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movieList, MainActivity.this);
                    recyclerView.setAdapter(movieAdapter); // Configurar adaptador si aún no está inicializado
                } else {
                    movieAdapter.updateMovies(movieList); // Actualizar lista de películas si el adaptador ya existe
                }
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadMovies(); // Recargar películas cuando se vuelve desde MovieDetailActivity o AddMovieActivity
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Recargar películas cuando se vuelve a la pantalla principal
    }
}
