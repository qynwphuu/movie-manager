package com.example.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

// table
@Entity
public class Movie {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Director is required")
    private String director;
    @Min(value = 1900, message = "Release year must be at least 1900")
    private int releaseYear;
    @NotBlank(message = "Genre is required")
    private String genre;
    @DecimalMin(value = "0.0", message = "Rating must be >= 0")
    @DecimalMax(value = "10.0", message = "Rating must be <= 10")
    private double rating;

    private boolean watched;

    // default constructor
    public Movie() {
    }

    // constructors
    public Movie(String title, String director, int releaseYear, String genre, double rating, boolean watched) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.watched = watched;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

}
