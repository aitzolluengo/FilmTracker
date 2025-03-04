package com.tzolas.filmtracker.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tzolas.filmtracker.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    // Obtener todas las películas
    @Query("SELECT * FROM movies ORDER BY title ASC")
    List<Movie> getAllMovies();

    // Insertar una nueva película
    @Insert
    void insertMovie(Movie movie);

    // Actualizar una película existente
    @Update
    void updateMovie(Movie movie);

    // Eliminar una película
    @Delete
    void deleteMovie(Movie movie);

    // Buscar películas por título
    @Query("SELECT * FROM movies WHERE title LIKE :query ORDER BY title ASC")
    List<Movie> searchMovies(String query);
}
