package com.moviemasti.showtime.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.moviemasti.showtime.dtos.ShowtimeDTO;
import com.moviemasti.showtime.entity.Showtime;
import com.moviemasti.showtime.service.ShowtimeService;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    
    @Autowired
    private ShowtimeService showtimeService;
    
    // Get all showtimes
    @GetMapping
    public List<Showtime> fetchAllShowtimes() {
        return showtimeService.getAllShowtimes();
    }
    
    // Get showtime by ID with bookings
    @GetMapping("/{id}")
    public Showtime getShowtimeById(@PathVariable Long id) {
        return showtimeService.getShowtimeById(id);
    }
    
    // Find shows by movie, city, and date
    @GetMapping("/search")
    public List<ShowtimeDTO> findShows(
            @RequestParam("movieId") Long movieId,
            @RequestParam("city") String city,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return showtimeService.findShowsByMovieAndCityAndDate(movieId, city, date);
    }
    
    // Get shows for a specific theater on a specific date
    @GetMapping("/theater/{theaterId}")
    public List<ShowtimeDTO> getTheaterShows(
            @PathVariable Long theaterId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return showtimeService.findShowsByTheaterAndDate(theaterId, date);
    }
    
    // Create a new showtime
    @PostMapping
    public ResponseEntity<Showtime> createShowtime(@RequestBody ShowtimeDTO showtimeDTO) {
        Showtime createdShowtime = showtimeService.createShowtime(showtimeDTO);
        
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdShowtime.getId())
                .toUri())
            .body(createdShowtime);
    }
    
    // Update an existing showtime
    @PutMapping("/{id}")
    public Showtime updateShowtime(
            @PathVariable Long id,
            @RequestBody ShowtimeDTO showtimeDTO) {
        
        return showtimeService.updateShowtime(id, showtimeDTO);
    }
    
    // Delete a showtime (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
    
    // Update seat inventory
    @PutMapping("/{id}/seats")
    public ResponseEntity<Void> updateSeatInventory(
            @PathVariable Long id,
            @RequestParam("adjustment") int adjustment) {
        
        showtimeService.updateSeatInventory(id, adjustment);
        return ResponseEntity.ok().build();
    }
    
    // Book seats for a showtime
    @PutMapping("/{id}/book-seats/{seats}")
    public void bookSeats(
            @PathVariable("id") Long showtimeId,
            @PathVariable("seats") Integer seats) {
        
        showtimeService.bookSeats(showtimeId, seats);
    }
    
    // Check if showtime exists
    @GetMapping("/{id}/exists")
    public Boolean validateShowtime(@PathVariable("id") Long id) {
        return showtimeService.doesShowtimeExist(id);
    }
    
    // Calculate price for tickets (including discounts)
    @GetMapping("/{id}/calculate-price")
    public double calculatePrice(
            @PathVariable Long id,
            @RequestParam("tickets") int numTickets) {
        
        Showtime showtime = showtimeService.getShowtimeById(id);
        return showtimeService.calculateTotalPrice(showtime, numTickets);
    }
}
