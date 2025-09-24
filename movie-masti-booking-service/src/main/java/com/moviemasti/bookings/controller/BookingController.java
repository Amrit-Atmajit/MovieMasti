package com.moviemasti.bookings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.moviemasti.bookings.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import com.moviemasti.bookings.dtos.BookingRequestDTO;
import com.moviemasti.bookings.dtos.BookingResponseDTO;
import com.moviemasti.bookings.dtos.BulkBookingRequestDTO;
import com.moviemasti.bookings.dtos.BulkBookingResponseDTO;
import com.moviemasti.bookings.service.BookingService;

/**
 * REST controller for handling booking-related operations.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    /**
     * Create a new booking
     * @param bookingRequest The booking details
     * @return The created booking with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequest) {
        BookingResponseDTO booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(booking.getId())
                        .toUri())
                .body(booking);
    }
    
    /**
     * Get a booking by ID
     * @param id The booking ID
     * @return The booking details
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    
    /**
     * Get all bookings for a user
     * @param userId The user ID
     * @return List of bookings for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }
    
    /**
     * Get all bookings for a showtime
     * @param showtimeId The showtime ID
     * @return List of bookings for the showtime
     */
    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByShowtimeId(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(bookingService.getBookingsByShowtimeId(showtimeId));
    }
    
    /**
     * Get bookings for a theater within a date range
     * @param theaterId The theater ID
     * @param startDate Start date (format: yyyy-MM-dd)
     * @param endDate End date (format: yyyy-MM-dd)
     * @return List of bookings for the theater in the date range
     */
    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByTheaterAndDateRange(
            @PathVariable Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        
        // Note: The actual date range filtering would be implemented in the service layer
        return ResponseEntity.ok(bookingService.getBookingsByTheaterAndDateRange(theaterId, startDate, endDate));
    }
    
    /**
     * Cancel a booking
     * @param id The booking ID to cancel
     * @return No content (204) on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Calculate the price for a potential booking
     * @param showtimeId The showtime ID
     * @param numTickets Number of tickets
     * @return The calculated price
     */
    @Operation(
            summary = "Calculate booking price",
            description = "Calculates the total price for a potential booking, including any applicable discounts.",
            tags = {"Utilities"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Price calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "number",
                                    format = "double",
                                    example = "380.0"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping(value = "/calculate-price", produces = "application/json")
    public ResponseEntity<Double> calculatePrice(
            @Parameter(description = "ID of the showtime", required = true, example = "1")
            @RequestParam Long showtimeId,
            @Parameter(description = "Number of tickets to book", required = true, example = "3")
            @RequestParam int numTickets) {
        
        double price = bookingService.calculatePrice(showtimeId, numTickets);
        return ResponseEntity.ok(price);
    }
    
    /**
     * Create multiple bookings in a single request
     * @param bulkRequest The bulk booking request
     * @return Response with results for each booking
     */
    @PostMapping("/bulk")
    public ResponseEntity<BulkBookingResponseDTO> createBulkBookings(
            @Valid @RequestBody BulkBookingRequestDTO bulkRequest) {
        
        BulkBookingResponseDTO response = bookingService.processBulkBooking(bulkRequest);
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
    }
    
    /**
     * Cancel multiple bookings
     * @param bookingIds List of booking IDs to cancel
     * @return Number of successfully cancelled bookings
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<Map<String, Object>> cancelMultipleBookings(
            @RequestParam List<Long> bookingIds) {
        
        int cancelledCount = bookingService.cancelMultipleBookings(bookingIds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalRequested", bookingIds.size());
        response.put("successfullyCancelled", cancelledCount);
        response.put("failed", bookingIds.size() - cancelledCount);
        
        return ResponseEntity.ok(response);
    }
}
