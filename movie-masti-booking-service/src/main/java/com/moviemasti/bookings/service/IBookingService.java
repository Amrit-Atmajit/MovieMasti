package com.moviemasti.bookings.service;

import java.util.List;

import com.moviemasti.bookings.dtos.BookingRequestDTO;
import com.moviemasti.bookings.dtos.BookingResponseDTO;
import com.moviemasti.bookings.dtos.BulkBookingRequestDTO;
import com.moviemasti.bookings.dtos.BulkBookingResponseDTO;

/**
 * Service interface for managing bookings
 */
public interface IBookingService {
    
    /**
     * Create a new booking
     * @param bookingRequest The booking details
     * @return The created booking DTO
     */
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);
    
    /**
     * Cancel a booking by ID
     * @param bookingId The ID of the booking to cancel
     */
    void cancelBooking(Long bookingId);
    
    /**
     * Get a booking by ID
     * @param id The booking ID
     * @return The booking DTO
     */
    BookingResponseDTO getBookingById(Long id);
    
    /**
     * Get all bookings for a user
     * @param userId The user ID
     * @return List of booking DTOs
     */
    List<BookingResponseDTO> getBookingsByUserId(Long userId);
    
    /**
     * Get all bookings for a showtime
     * @param showtimeId The showtime ID
     * @return List of booking DTOs
     */
    List<BookingResponseDTO> getBookingsByShowtimeId(Long showtimeId);
    
    /**
     * Get bookings for a theater within a date range
     * @param theaterId The theater ID
     * @param startDate Start date (format: yyyy-MM-dd)
     * @param endDate End date (format: yyyy-MM-dd)
     * @return List of booking DTOs
     */
    List<BookingResponseDTO> getBookingsByTheaterAndDateRange(Long theaterId, String startDate, String endDate);
    
    /**
     * Calculate the price for a potential booking
     * @param showtimeId The showtime ID
     * @param numTickets Number of tickets
     * @return The calculated price
     */
    double calculatePrice(Long showtimeId, int numTickets);
    
    /**
     * Process multiple bookings in a single transaction
     * @param bulkRequest The bulk booking request containing multiple booking requests
     * @return Bulk booking response with results for each booking
     */
    BulkBookingResponseDTO processBulkBooking(BulkBookingRequestDTO bulkRequest);
    
    /**
     * Cancel multiple bookings
     * @param bookingIds List of booking IDs to cancel
     * @return Number of successfully cancelled bookings
     */
    int cancelMultipleBookings(List<Long> bookingIds);
}
