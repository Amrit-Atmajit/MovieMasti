package com.moviemasti.bookings.validation;

import com.moviemasti.bookings.dtos.BookingRequestDTO;
import com.moviemasti.bookings.exception.InvalidBookingException;
import com.moviemasti.bookings.service.ShowtimeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Validates booking requests before processing
 */
//@Component
//@RequiredArgsConstructor
public class BookingValidator {
    
    private ShowtimeClient showtimeClient;
    
    /**
     * Validates a booking request
     * @param request The booking request to validate
     * @throws InvalidBookingException if the booking request is invalid
     */
    public void validateBookingRequest(BookingRequestDTO request) {
        if (request == null) {
            throw new InvalidBookingException("Booking request cannot be null");
        }
        
        validateUserId(request.getUserId());
        validateShowtimeId(request.getShowtimeId());
        validateTheaterId(request.getTheaterId());
        validateMovieId(request.getMovieId());
        validateSeatNumbers(request.getSeatNumbers());
        validateShowDateTime(request.getShowDateTime());
        
        // Additional validation that requires service calls
        validateShowtimeExists(request.getShowtimeId());
        validateShowtimeNotInPast(request.getShowDateTime());
    }
    
    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidBookingException("Invalid user ID: " + userId);
        }
    }
    
    private void validateShowtimeId(Long showtimeId) {
        if (showtimeId == null || showtimeId <= 0) {
            throw new InvalidBookingException("Invalid showtime ID: " + showtimeId);
        }
    }
    
    private void validateTheaterId(Long theaterId) {
        if (theaterId == null || theaterId <= 0) {
            throw new InvalidBookingException("Invalid theater ID: " + theaterId);
        }
    }
    
    private void validateMovieId(Long movieId) {
        if (movieId == null || movieId <= 0) {
            throw new InvalidBookingException("Invalid movie ID: " + movieId);
        }
    }
    
    private void validateSeatNumbers(List<String> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new InvalidBookingException("At least one seat must be selected");
        }
        
        // Validate seat number format (e.g., A1, B12, etc.)
        String seatPattern = "^[A-Za-z]\\d+$";
        for (String seat : seatNumbers) {
            if (seat == null || !seat.matches(seatPattern)) {
                throw new InvalidBookingException("Invalid seat number format: " + seat);
            }
        }
    }
    
    /*private void validateShowDateTime(LocalDateTime showDateTime) {
        if (showDateTime == null) {
            throw new InvalidBookingException("Show date/time cannot be null");
        }
        
        if (showDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidBookingException("Cannot book a show that has already started or ended");
        }
    }*/

    private void validateShowDateTime(String showDateTimeStr) {
        if (showDateTimeStr == null || showDateTimeStr.trim().isEmpty()) {
            throw new InvalidBookingException("Show date and time is required");
        }

        try {
            LocalDateTime showDateTime = LocalDateTime.parse(showDateTimeStr);
            // Add any additional validation for the date here
            if (showDateTime.isBefore(LocalDateTime.now())) {
                throw new InvalidBookingException("Show time cannot be in the past");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidBookingException("Invalid date-time format. Please use yyyy-MM-ddTHH:mm:ss format");
        }
    }
    
    private void validateShowtimeExists(Long showtimeId) {
        Boolean exists = showtimeClient.doesShowtimeExist(showtimeId);
        if (exists == null || !exists) {
            throw new InvalidBookingException("Showtime not found with ID: " + showtimeId);
        }
    }
    
    private void validateShowtimeNotInPast(String showDateTimeStr) {
        try {
            LocalDateTime showDateTime = LocalDateTime.parse(showDateTimeStr);
            if (showDateTime.isBefore(LocalDateTime.now())) {
                throw new InvalidBookingException("Cannot book a show that has already started or ended");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidBookingException("Invalid date-time format. Please use ISO-8601 format (e.g., 2023-01-01T18:30:00)");
        }
    }
    
    /**
     * Validates if a booking can be cancelled
     * @param bookingDateTime The booking date/time
     * @param showDateTime The show date/time
     * @throws InvalidBookingException if the booking cannot be cancelled
     */
    public void validateBookingCancellation(LocalDateTime bookingDateTime, LocalDateTime showDateTime) {
        if (LocalDateTime.now().isAfter(showDateTime)) {
            throw new InvalidBookingException("Cannot cancel a booking for a show that has already started or ended");
        }
        
        // Example: Don't allow cancellation within 1 hour of showtime
        if (LocalDateTime.now().plusHours(1).isAfter(showDateTime)) {
            throw new InvalidBookingException("Cannot cancel booking within 1 hour of showtime");
        }
    }
}
