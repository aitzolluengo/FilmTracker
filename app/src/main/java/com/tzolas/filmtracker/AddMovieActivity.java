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


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.content.Context;

public class AddMovieActivity extends AppCompatActivity {

    // UI Components
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

        // Obtener instancia de la base de datos
        movieDatabase = MovieDatabase.getInstance(this);

        // Configurar botón para guardar película
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovie();
            }
        });
    }

    // Método para guardar una nueva película en la base de datos
    private void saveMovie() {
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
        Movie newMovie = new Movie(title, director, genre, year, rating);

        new Thread(() -> {
            movieDatabase.movieDao().insertMovie(newMovie);
            runOnUiThread(() -> {
                Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent()); // Notificar a MainActivity
                finish(); // Cerrar la actividad y volver a MainActivity
            });
        }).start();
    }
    public void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default", "FilmTracker Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new Notification.Builder(this, "default")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification) // Asegúrate de tener un icono
                .build();

        notificationManager.notify(1, notification);
    }
}
