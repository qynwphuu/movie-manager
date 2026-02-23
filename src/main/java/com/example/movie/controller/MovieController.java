package com.example.movie.controller;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Get all movies
    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }

    // Add a new movie
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "add-movie";
    }

    @PostMapping("/add")
    public String saveMovie(@Valid @ModelAttribute Movie movie, BindingResult result) {
        if (result.hasErrors()) {
            return "add-movie";
        } // check for validation errors

        movieRepository.save(movie);
        return "redirect:/movies";
    }

    // Delete a movie
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        return "redirect:/movies";
    }

    // Edit a movie
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
        model.addAttribute("movie", movie);
        return "edit-movie";
    }

    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id, @Valid @ModelAttribute Movie movie, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-movie";
        }
        movie.setId(id); // make sure to set the ID for update
        movieRepository.save(movie); // update
        return "redirect:/movies";
    }

}
