package com.moviemasti.movies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviemasti.movies.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
