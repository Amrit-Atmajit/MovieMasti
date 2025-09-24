package com.moviemasti.showtime.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moviemasti.showtime.entity.Showtime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    
    @Query("SELECT s FROM Showtime s WHERE " +
           "s.movieId = :movieId AND " +
           "LOWER(s.city) = LOWER(:city) AND " +
           "s.showDate BETWEEN :startDate AND :endDate AND " +
           "s.isActive = true")
    List<Showtime> findByMovieIdAndCityAndShowDateBetweenAndIsActiveTrue(
            @Param("movieId") Long movieId,
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Showtime s WHERE " +
           "s.theaterId = :theaterId AND " +
           "s.showDate BETWEEN :startDate AND :endDate AND " +
           "s.isActive = true")
    List<Showtime> findByTheaterIdAndShowDateBetweenAndIsActiveTrue(
            @Param("theaterId") Long theaterId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    List<Showtime> findByTheaterIdAndIsActiveTrue(Long theaterId);
    
    @Query("SELECT s FROM Showtime s WHERE " +
           "s.theaterId = :theaterId AND " +
           "s.movieId = :movieId AND " +
           "s.showDate BETWEEN :startDate AND :endDate AND " +
           "s.isActive = true")
    List<Showtime> findByTheaterIdAndMovieIdAndShowDateBetweenAndIsActiveTrue(
            @Param("theaterId") Long theaterId,
            @Param("movieId") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
