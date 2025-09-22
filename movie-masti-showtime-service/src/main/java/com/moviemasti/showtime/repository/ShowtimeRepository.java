package com.moviemasti.showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviemasti.showtime.entity.Showtime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

}
