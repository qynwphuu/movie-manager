package com.example.movie.controller;

import com.example.movie.model.Genre;
import com.example.movie.model.Movie;
import com.example.movie.repository.GenreRepository;
import com.example.movie.repository.MovieRepository;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {
    // inject repositories
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    // constructor injection
    public MovieController(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    // Get all movies
    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }

    // Add a new movie (GET)
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        Movie movie = new Movie();
        movie.setGenre(new Genre()); // initialize for th:field="*{genre.id}" binding
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genreRepository.findAll());
        return "add-movie";
    }

    // Add a new movie (POST)
    @PostMapping("/add")
    public String saveMovie(@Valid @ModelAttribute Movie movie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            return "add-movie";
        } // check for validation errors

        // Look up the full Genre entity from the submitted genre ID
        if (movie.getGenre() != null && movie.getGenre().getId() != null) {
            Genre genre = genreRepository.findById(movie.getGenre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid genre"));
            movie.setGenre(genre);
        }
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    // Delete a movie
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        return "redirect:/movies";
    }

    // Edit a movie (GET)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genreRepository.findAll());
        return "edit-movie";
    }

    // Edit a movie (POST)
    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable Long id, @Valid @ModelAttribute Movie movie, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            return "edit-movie";
        }
        movie.setId(id); // make sure to set the ID for update
        // Look up the full Genre entity from the submitted genre ID
        if (movie.getGenre() != null && movie.getGenre().getId() != null) {
            Genre genre = genreRepository.findById(movie.getGenre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid genre"));
            movie.setGenre(genre);
        }
        movieRepository.save(movie); // update
        return "redirect:/movies";
    }

}
