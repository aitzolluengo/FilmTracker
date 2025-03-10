package com.tzolas.filmtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieDatabase movieDatabase;
    private List<Movie> movieList = new ArrayList<>();

    // Filtros de Género y Año
    private Spinner spinnerGenre, spinnerYear;
    private String selectedGenre = "Todos";
    private String selectedYear = "Todos";

    // DrawerLayout y NavigationView
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

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
        Button btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        });

        // Configurar los Spinners de Filtro
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerYear = findViewById(R.id.spinnerYear);

        // Listener para el filtro por género
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenre = parent.getItemAtPosition(position).toString();
                filterMovies();  // Asegúrate de que este método esté bien definido
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Si no se selecciona nada, no hacer nada
            }
        });

        // Listener para el filtro por año
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                filterMovies();  // Asegúrate de que este método esté bien definido
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Si no se selecciona nada, no hacer nada
            }
        });

        // Configuración del NavigationView y DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar el Toolbar y el NavigationView
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Método para cargar películas desde la base de datos y mostrarlas en la lista
    private void loadMovies() {
        new Thread(() -> {
            movieList = movieDatabase.movieDao().getAllMovies();
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

    // Filtro de películas por género y año
    private void filterMovies() {
        // Filtrado de películas basado en el género y año seleccionados
        new Thread(() -> {
            List<Movie> filteredMovies = new ArrayList<>();
            for (Movie movie : movieList) {
                boolean matchesGenre = selectedGenre.equals("Todos") || movie.getGenre().toLowerCase().contains(selectedGenre.toLowerCase());
                boolean matchesYear = checkYearFilter(movie.getYear());

                // Si la película coincide con el filtro de género y el filtro de año
                if (matchesGenre && matchesYear) {
                    filteredMovies.add(movie);
                }
            }
            runOnUiThread(() -> movieAdapter.updateMovies(filteredMovies));
        }).start();
    }

    private boolean checkYearFilter(int movieYear) {
        // Lógica para verificar si el año de la película coincide con el filtro seleccionado
        switch (selectedYear) {
            case "2023": return movieYear == 2023;
            case "2022": return movieYear == 2022;
            case "2021": return movieYear == 2021;
            case "2020": return movieYear == 2020;
            case "2010 - 2019": return movieYear >= 2010 && movieYear <= 2019;
            case "2000 - 2009": return movieYear >= 2000 && movieYear <= 2009;
            case "1990 - 1999": return movieYear >= 1990 && movieYear <= 1999;
            case "1980 - 1989": return movieYear >= 1980 && movieYear <= 1989;
            case "1970 - 1979": return movieYear >= 1970 && movieYear <= 1979;
            case "Antes de 1970": return movieYear < 1970;
            default: return true; // "Todos"
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Añadir funcionalidades para las opciones del menú
        if (item.getItemId() == R.id.action_sort_title) {
            sortMoviesByTitle();
            return true;
        } else if (item.getItemId() == R.id.action_sort_year) {
            sortMoviesByYear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortMoviesByTitle() {
        Collections.sort(movieList, Comparator.comparing(Movie::getTitle));
        movieAdapter.updateMovies(movieList);
    }

    private void sortMoviesByYear() {
        Collections.sort(movieList, Comparator.comparingInt(Movie::getYear));
        movieAdapter.updateMovies(movieList);
    }
}
