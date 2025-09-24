package com.moviemasti.bookings.service;

import com.moviemasti.bookings.dtos.ShowtimeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moviemasti.bookings.config.FeignClientConfig;

import java.util.List;

/**
 * Feign client for communicating with the showtime service
 */
@FeignClient(name = "showtime-service", path = "/api/showtimes", configuration = FeignClientConfig.class)
public interface ShowtimeClient {
    
    /**
     * Check if a showtime exists
     * @param id The showtime ID
     * @return true if the showtime exists, false otherwise
     */
    @GetMapping("/{id}/exists")
    Boolean doesShowtimeExist(@PathVariable("id") Long id);
    
    /**
     * Book seats for a showtime
     * @param showtimeId The showtime ID
     * @param seats Number of seats to book
     */
    @PutMapping("/{showtimeId}/book-seats/{seats}")
    void bookSeats(@PathVariable("showtimeId") Long showtimeId, @PathVariable("seats") Integer seats);
    
    /**
     * Update seat inventory for a showtime
     * @param showtimeId The showtime ID
     * @param adjustment Number of seats to adjust (positive or negative)
     */
    @PutMapping("/{showtimeId}/seats")
    void updateSeatInventory(
            @PathVariable("showtimeId") Long showtimeId,
            @RequestParam("adjustment") int adjustment);
    
    /**
     * Get showtime details with bookings
     * @param id The showtime ID
     * @return Showtime details with bookings
     */
    @GetMapping("/{id}")
    ShowtimeDTO getShowtimeWithBookings(@PathVariable("id") Long id);
    
    /**
     * Get all showtimes for a movie in a city on a specific date
     * @param movieId The movie ID
     * @param city The city name
     * @param date The date in yyyy-MM-dd format
     * @return List of showtimes
     */
    @GetMapping("/search")
    List<ShowtimeDTO> findShowsByMovieAndCityAndDate(
            @RequestParam("movieId") Long movieId,
            @RequestParam("city") String city,
            @RequestParam("date") String date);
    
    /**
     * Get all showtimes for a theater on a specific date
     * @param theaterId The theater ID
     * @param date The date in yyyy-MM-dd format
     * @return List of showtimes
     */
    @GetMapping("/theater/{theaterId}")
    List<ShowtimeDTO> findShowsByTheaterAndDate(
            @PathVariable("theaterId") Long theaterId,
            @RequestParam("date") String date);
    
    /**
     * Calculate the price for a booking
     * @param showtimeId The showtime ID
     * @param numTickets Number of tickets
     * @return The calculated price
     */
    @GetMapping("/{showtimeId}/calculate-price")
    double calculatePrice(
            @PathVariable("showtimeId") Long showtimeId,
            @RequestParam("tickets") int numTickets);
}
