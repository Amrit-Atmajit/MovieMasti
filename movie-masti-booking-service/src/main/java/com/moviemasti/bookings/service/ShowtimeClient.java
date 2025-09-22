package com.moviemasti.bookings.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.moviemasti.bookings.config.FeignClientConfig;

@FeignClient(name = "showtime-service", path = "/api/showtimes", configuration = FeignClientConfig.class)
public interface ShowtimeClient {
	
	@GetMapping("/{id}/exists")
    Boolean doesShowtimeExist(@PathVariable("id") Long id);
	
	@PutMapping("/{showtimeId}/book-seats/{seats}")
	void bookSeats(@PathVariable("showtimeId") Long showtimeId, @PathVariable("seats") Integer seats);
}
