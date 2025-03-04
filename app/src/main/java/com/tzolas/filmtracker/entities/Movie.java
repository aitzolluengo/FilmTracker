package com.tzolas.filmtracker.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@com.tzolas.filmtracker.entities.Entity(tableName = "movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String director;
    private int year;
    private String genre;
    private float rating;

    // Constructor
    public Movie(String title, String director, int year, String genre, float rating) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}

