package com.tzolas.filmtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;
import com.tzolas.filmtracker.utils.NotificationHelper;
import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private MovieDatabase database;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button btnAddMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarModoOscuro();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        // Configurar Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Inicializar la base de datos
        database = MovieDatabase.getInstance(this);
        movieList = database.movieDao().getAllMovies();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);

        // Configurar botón de añadir película
        btnAddMovie = findViewById(R.id.btnAddMovie);
        btnAddMovie.setOnClickListener(v -> openAddMovieActivity());

        // Verificar permisos de notificaciones
        checkNotificationPermission();
    }

    private void aplicarModoOscuro() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void openAddMovieActivity() {
        Intent intent = new Intent(this, AddMovieActivity.class);
        startActivity(intent);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_add_movie) {
            openAddMovieActivity();
        } else if (id == R.id.nav_exit) {
            finishAffinity();
        }

        drawerLayout.closeDrawers();
        return true;
    }


    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }
}
