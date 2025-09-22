package com.moviemasti.bookings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviemasti.bookings.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserId(Long userId);
	List<Booking> findByShowtimeId(Long showtimeId);
}
