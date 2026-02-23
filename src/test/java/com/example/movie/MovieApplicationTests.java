package com.example.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.movie.model.Movie;
import com.example.movie.model.Genre;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.GenreRepository;

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository; // JpaRepository

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll(); // reset DB
        genreRepository.deleteAll();

        // create genres
        genreRepository.save(new Genre("Sci-Fi"));
        genreRepository.save(new Genre("Action"));
    }

    @Test
    void testAddMovie() {
        Genre sciFi = genreRepository.findByName("Sci-Fi").orElseThrow();
        Movie movie = new Movie("Inception", "Nolan", 2010, sciFi, 9.0, true);
        movieRepository.save(movie); // save
        assertEquals(1, movieRepository.findAll().size()); // check if there is 1 movie
    }

    @Test
    void testDeleteMovie() {
        Genre sciFi = genreRepository.findByName("Sci-Fi").orElseThrow();
        Movie movie = new Movie("Interstellar", "Nolan", 2014, sciFi, 8.5, false);
        movieRepository.save(movie);
        movieRepository.delete(movie); // delete
        assertTrue(movieRepository.findAll().isEmpty());
    }

    @Test
    void testUpdateMovie() {
        Genre action = genreRepository.findByName("Action").orElseThrow();
        Movie movie = new Movie("The Dark Knight", "Nolan", 2008, action, 9.0, true);
        movieRepository.save(movie);
        movie.setRating(9.5); // update rating
        movieRepository.save(movie); // save update
        Movie updatedMovie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals(9.5, updatedMovie.getRating());
    }

}
