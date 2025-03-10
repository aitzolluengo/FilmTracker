package com.tzolas.filmtracker.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String director;
    private String genre;
    private int year;
    private float rating;

    // Constructor
    public Movie(String title, String director, String genre, int year, float rating) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}
