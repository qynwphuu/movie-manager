package com.example.movie;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
			mergeDuplicateGenres(genreRepository, movieRepository, "Action");
			mergeDuplicateGenres(genreRepository, movieRepository, "Comedy");
			mergeDuplicateGenres(genreRepository, movieRepository, "Drama");
			mergeDuplicateGenres(genreRepository, movieRepository, "Horror");
			mergeDuplicateGenres(genreRepository, movieRepository, "Sci-Fi");
			mergeDuplicateMovies(movieRepository);

			// Preload genres first
			Genre action = genreRepository.findFirstByNameOrderByIdAsc("Action")
					.orElseGet(() -> genreRepository.save(new Genre("Action")));
			Genre comedy = genreRepository.findFirstByNameOrderByIdAsc("Comedy")
					.orElseGet(() -> genreRepository.save(new Genre("Comedy")));
			Genre drama = genreRepository.findFirstByNameOrderByIdAsc("Drama")
					.orElseGet(() -> genreRepository.save(new Genre("Drama")));
			Genre horror = genreRepository.findFirstByNameOrderByIdAsc("Horror")
					.orElseGet(() -> genreRepository.save(new Genre("Horror")));
			Genre sciFi = genreRepository.findFirstByNameOrderByIdAsc("Sci-Fi")
					.orElseGet(() -> genreRepository.save(new Genre("Sci-Fi")));

			// Then save sample movies
			if (movieRepository.count() == 0) {
				movieRepository.save(new Movie("Inception", "Nolan", 2010, sciFi, 9.0, true));
				movieRepository.save(new Movie("Interstellar", "Nolan", 2014, sciFi, 8.5, false));
				movieRepository.save(new Movie("Die Hard", "McTiernan", 1988, action, 8.7, true));
			}
		};
	}

	private void mergeDuplicateMovies(MovieRepository movieRepository) {
		List<Movie> allMovies = movieRepository.findAll();
		Map<String, Movie> uniqueMovies = new LinkedHashMap<>();
		List<Movie> duplicates = new ArrayList<>();

		for (Movie movie : allMovies) {
			String genreName = movie.getGenre() != null ? movie.getGenre().getName() : "";
			String key = movie.getTitle() + "|" + movie.getDirector() + "|" + movie.getReleaseYear() + "|"
					+ genreName + "|" + movie.getRating() + "|" + movie.isWatched();

			if (uniqueMovies.containsKey(key)) {
				duplicates.add(movie);
			} else {
				uniqueMovies.put(key, movie);
			}
		}

		if (!duplicates.isEmpty()) {
			movieRepository.deleteAll(duplicates);
		}
	}

	private void mergeDuplicateGenres(GenreRepository genreRepository, MovieRepository movieRepository,
			String genreName) {
		List<Genre> matchingGenres = genreRepository.findAllByNameOrderByIdAsc(genreName);
		if (matchingGenres.size() <= 1) {
			return;
		}

		Genre keep = matchingGenres.get(0);
		for (int i = 1; i < matchingGenres.size(); i++) {
			Genre duplicate = matchingGenres.get(i);
			List<Movie> moviesUsingDuplicate = movieRepository.findByGenre(duplicate);
			for (Movie movie : moviesUsingDuplicate) {
				movie.setGenre(keep);
			}
			movieRepository.saveAll(moviesUsingDuplicate);
			genreRepository.delete(duplicate);
		}
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
			if (repository.findByUsername("user") == null) {
				repository.save(user1);
			}
			if (repository.findByUsername("admin") == null) {
				repository.save(user2);
			}
		};
	}

}
