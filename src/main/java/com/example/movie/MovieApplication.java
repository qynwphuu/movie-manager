package com.example.movie;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.movie.model.Genre;
import com.example.movie.model.Movie;
import com.example.movie.model.User;
import com.example.movie.repository.GenreRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.UserRepository;

@SpringBootApplication
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(GenreRepository genreRepository, MovieRepository movieRepository) {
		return args -> {
			// Preload genres first
			Genre action = genreRepository.save(new Genre("Action"));
			Genre comedy = genreRepository.save(new Genre("Comedy"));
			Genre drama = genreRepository.save(new Genre("Drama"));
			Genre horror = genreRepository.save(new Genre("Horror"));
			Genre sciFi = genreRepository.save(new Genre("Sci-Fi"));

			// Then save sample movies
			movieRepository.save(new Movie("Inception", "Nolan", 2010, sciFi, 9.0, true));
			movieRepository.save(new Movie("Interstellar", "Nolan", 2014, sciFi, 8.5, false));
			movieRepository.save(new Movie("Die Hard", "McTiernan", 1988, action, 8.7, true));
		};
	}

	@Bean
	public CommandLineRunner initUsers(UserRepository repository) {
		return args -> {
			User user1 = new User("user", "user@example.com",
					"$2a$12$j/SgoiwjvY2ssbp5G/PbnuAamMAqGrc4hriMxvpFqjsyA77aFnXAe", "USER");
			// pass is "password"
			User user2 = new User("admin", "admin@example.com",
					"$2a$12$loeJmqaj30YeodKSDhiAHuGfBRhGuIouZPUvg0IjxHKnP765KArTa", "ADMIN");
			// pass is "admin"
			repository.save(user1);
			repository.save(user2);
		};
	}

}
