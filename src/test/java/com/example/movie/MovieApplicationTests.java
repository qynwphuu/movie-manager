package com.example.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.movie.model.Movie;
import com.example.movie.model.Genre;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.GenreRepository;

@SpringBootTest
class MovieApplicationTests {

    @Autowired
    private MovieRepository movieRepository; // JpaRepository

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(movieRepository).isNotNull();
        assertThat(genreRepository).isNotNull();
    }

    @BeforeEach
    public void setup() throws Exception {
        // reset DB before each test
        movieRepository.deleteAll();
        genreRepository.deleteAll();

        // create genres
        genreRepository.save(new Genre("Sci-Fi"));
        genreRepository.save(new Genre("Action"));
    }

    @Test
    public void testAddMovie() throws Exception {
        Genre sciFi = genreRepository.findByName("Sci-Fi").orElseThrow();
        Movie movie = new Movie("Inception", "Nolan", 2010, sciFi, 9.0, true);
        movieRepository.save(movie); // save
        assertEquals(1, movieRepository.findAll().size()); // check if there is 1 movie
    }

    @Test
    public void testDeleteMovie() throws Exception {
        Genre sciFi = genreRepository.findByName("Sci-Fi").orElseThrow();
        Movie movie = new Movie("Interstellar", "Nolan", 2014, sciFi, 8.5, false);
        movieRepository.save(movie);
        movieRepository.delete(movie); // delete
        assertTrue(movieRepository.findAll().isEmpty());
    }

    @Test
    public void testUpdateMovie() throws Exception {
        Genre action = genreRepository.findByName("Action").orElseThrow();
        Movie movie = new Movie("The Dark Knight", "Nolan", 2008, action, 9.0, true);
        movieRepository.save(movie);
        movie.setRating(9.5); // update rating
        movieRepository.save(movie); // save update
        Movie updatedMovie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals(9.5, updatedMovie.getRating());
    }

    @Test
    public void testFindAllMovies() throws Exception {
        Genre sciFi = genreRepository.findByName("Sci-Fi").orElseThrow();
        Movie movie1 = new Movie("Inception", "Nolan", 2010, sciFi, 9.0, true);
        Movie movie2 = new Movie("Interstellar", "Nolan", 2014, sciFi, 8.5, false);
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        assertEquals(2, movieRepository.findAll().size()); // check if there are 2 movies
    }

}
