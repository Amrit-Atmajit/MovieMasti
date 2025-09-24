package com.moviemasti.movies.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviemasti.movies.entity.Movie;
import com.moviemasti.movies.enums.Genre;
import com.moviemasti.movies.enums.Language;
import com.moviemasti.movies.repository.MovieRepository;

@Service
public class AdminMovieService {

	@Autowired
	private MovieRepository movieRepository;

	public Movie createMovie(Movie movie) {
		return movieRepository.save(movie);
	}

	public Movie updateMovie(Long id, Movie updatedMovie) {
		return movieRepository.findById(id)
			.map(existingMovie -> {
				existingMovie.setTitle(updatedMovie.getTitle());
				existingMovie.setDescription(updatedMovie.getDescription());
				existingMovie.setDuration(updatedMovie.getDuration());
				existingMovie.setRating(updatedMovie.getRating());
				existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
				existingMovie.setGenre(updatedMovie.getGenre());
				existingMovie.setLanguage(updatedMovie.getLanguage());
				existingMovie.setCertificate(updatedMovie.getCertificate());
				existingMovie.setCast(updatedMovie.getCast());
				existingMovie.setDirector(updatedMovie.getDirector());
				return movieRepository.save(existingMovie);
			})
			.orElseThrow(() -> new IllegalArgumentException("Movie with ID " + id + " not found"));
	}

	public void deleteMovie(Long id) {
		if (movieRepository.existsById(id)) {
			movieRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Movie with ID " + id + " not found");
		}
	}
}
