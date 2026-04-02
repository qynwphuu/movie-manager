package com.example.movie.controller;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieRepository movieRepository;

    public MovieRestController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // GET all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // GET one movie
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // POST create movie
    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    // DELETE movie
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
    }
}