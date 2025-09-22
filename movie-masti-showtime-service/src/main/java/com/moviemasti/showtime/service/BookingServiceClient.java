package com.moviemasti.showtime.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moviemasti.showtime.config.FeignClientConfig;
import com.moviemasti.showtime.dtos.BookingDTO;

@FeignClient(name = "booking-service", path = "/api/bookings", configuration = FeignClientConfig.class)
public interface BookingServiceClient {
	@GetMapping
    List<BookingDTO> getBookingsByShowtimeId(@RequestParam("showtimeId") Long showtimeId);
}
