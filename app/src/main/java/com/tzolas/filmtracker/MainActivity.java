package com.tzolas.filmtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
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
        loadMovies(); // Carga las películas al iniciar la actividad

        // Botón para añadir nuevas películas
        findViewById(R.id.btnAddMovie).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddMovieActivity.class)));
    }

    // Método para cargar películas desde la base de datos y mostrarlas en la lista
    private void loadMovies() {
        new Thread(() -> {
            List<Movie> movieList = movieDatabase.movieDao().getAllMovies(); // Obtener todas las películas de la BD
            runOnUiThread(() -> {
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movieList, MainActivity.this, this::onMovieClick, this::deleteMovie);
                    recyclerView.setAdapter(movieAdapter); // Configurar adaptador si aún no está inicializado
                } else {
                    movieAdapter.updateMovies(movieList); // Actualizar lista de películas si el adaptador ya existe
                }
            });
        }).start();
    }

    // Método para eliminar una película de la base de datos
    public void deleteMovie(Movie movie) {
        new Thread(() -> {
            movieDatabase.movieDao().deleteMovie(movie); // Eliminar la película de la BD
            runOnUiThread(this::loadMovies); // Recargar la lista en la UI
        }).start();
    }

    // Método para añadir una película a la base de datos
    public void addMovie(Movie movie) {
        new Thread(() -> {
            movieDatabase.movieDao().insertMovie(movie); // Insertar la película en la BD
            runOnUiThread(this::loadMovies); // Recargar la lista en la UI
        }).start();
    }

    // Método para actualizar una película en la base de datos
    public void updateMovie(Movie movie) {
        new Thread(() -> {
            movieDatabase.movieDao().updateMovie(movie); // Actualizar la película en la BD
            runOnUiThread(this::loadMovies); // Recargar la lista en la UI
        }).start();
    }

    // Método para manejar clics en los elementos de la lista
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE_ID", movie.getId()); // Pasar el ID de la película
        startActivity(intent);
    }
    // Método para buscar películas en la base de datos por título
    public void searchMovies(String query) {
        new Thread(() -> {
            List<Movie> filteredMovies = movieDatabase.movieDao().searchMovies(query);
            runOnUiThread(() -> {
                if (movieAdapter != null) {
                    movieAdapter.updateMovies(filteredMovies); // Actualizar la lista con los resultados de búsqueda
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Recargar películas cuando se vuelve a la pantalla principal
    }
    // Agregar barra de búsqueda en el menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMovies(newText);
                return false;
            }
        });
        return true;
    }
}
