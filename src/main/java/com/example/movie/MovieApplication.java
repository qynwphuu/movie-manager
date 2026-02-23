package com.example.movie;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;

@SpringBootApplication
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

	// Add some sample movies on startup
	@Component
	public static class DataLoader implements CommandLineRunner {

		private final MovieRepository movieRepository;

		public DataLoader(MovieRepository movieRepository) {
			this.movieRepository = movieRepository;
		}

		@Override
		public void run(String... args) throws Exception {
			movieRepository.save(new Movie("Inception", "Nolan", 2010, "Sci-fi", 9.0, true));
			movieRepository.save(new Movie("Interstellar", "Nolan", 2014, "Sci-fi", 8.5, false));
		}
	}

}
