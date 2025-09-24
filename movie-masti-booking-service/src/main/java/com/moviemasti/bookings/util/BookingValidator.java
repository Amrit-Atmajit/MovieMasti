package com.moviemasti.bookings.util;

import com.moviemasti.bookings.exception.InvalidBookingException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Utility class for validating booking-related operations
 */
@Component
public class BookingValidator {
    
    /**
     * Validates if a booking can be cancelled
     * @param bookingDate The date when the booking was made
     * @param showDateTime The date and time of the show
     * @throws InvalidBookingException if the booking cannot be cancelled
     */
    public void validateBookingCancellation(LocalDateTime bookingDate, LocalDateTime showDateTime) {
        LocalDateTime now = LocalDateTime.now();
        
        // Cannot cancel after show has started
        if (now.isAfter(showDateTime) || now.isEqual(showDateTime)) {
            throw new InvalidBookingException("Cannot cancel booking after show has started");
        }
        
        // Example: Cannot cancel within 1 hour of show time
        if (now.plusHours(1).isAfter(showDateTime)) {
            throw new InvalidBookingException("Cannot cancel booking within 1 hour of show time");
        }
    }
}
