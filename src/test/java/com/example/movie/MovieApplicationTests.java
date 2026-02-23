package com.example.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository; // JpaRepository

    @BeforeEach
    void setup() {
        movieRepository.deleteAll(); // reset DB before test
    }

    @Test
    void testAddMovie() {
        Movie movie = new Movie("Inception", "Nolan", 2010, "Sci-fi", 9.0, true);
        movieRepository.save(movie); // save
        assertEquals(1, movieRepository.findAll().size()); // check if there is 1 movie
    }

    @Test
    void testDeleteMovie() {
        Movie movie = new Movie("Interstellar", "Nolan", 2014, "Sci-fi", 8.5, false);
        movieRepository.save(movie);
        movieRepository.delete(movie); // delete
        assertTrue(movieRepository.findAll().isEmpty());
    }

    @Test
    void testUpdateMovie() {
        Movie movie = new Movie("The Dark Knight", "Nolan", 2008, "Action", 9.0, true);
        movieRepository.save(movie);
        movie.setRating(9.5); // update rating
        movieRepository.save(movie); // save update
        Movie updatedMovie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals(9.5, updatedMovie.getRating());
    }

}
