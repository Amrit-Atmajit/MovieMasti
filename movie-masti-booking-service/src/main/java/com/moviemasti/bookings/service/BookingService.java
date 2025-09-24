package com.moviemasti.bookings.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.moviemasti.bookings.dtos.*;
import com.moviemasti.bookings.util.BookingModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviemasti.bookings.entity.Booking;
import com.moviemasti.bookings.enums.BookingStatus;
import com.moviemasti.bookings.exception.InvalidBookingException;
import com.moviemasti.bookings.exception.ResourceNotFoundException;
import com.moviemasti.bookings.exception.ShowtimeNotFoundException;
import com.moviemasti.bookings.util.BookingValidator;
import com.moviemasti.bookings.repository.BookingRepository;

import feign.FeignException;

/**
 * Implementation of the booking service
 */
@Service
public class BookingService implements IBookingService {
    
    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private ShowtimeClient showtimeClient;
    
    @Autowired
    private BookingModelMapper modelMapper;
    
    @Autowired
    private BookingValidator bookingValidator;
    
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        try {
            // 1. Validate showtime and get showtime details
            var showtime = showtimeClient.getShowtimeWithBookings(bookingRequest.getShowtimeId());
            
            // 2. Check seat availability
            if (showtime.getAvailableSeats() < bookingRequest.getNumberOfSeats()) {
                throw new InvalidBookingException("Not enough seats available");
            }
            
            // 3. Calculate price with discounts
            double basePrice = showtime.getPricePerSeat() * bookingRequest.getNumberOfSeats();
            double discount = calculateDiscount(showtime, bookingRequest.getNumberOfSeats());
            double totalPrice = Math.max(0, basePrice - discount);
            
            // 4. Create booking
            Booking booking = new Booking();
            booking.setUserId(bookingRequest.getUserId());
            booking.setShowtimeId(bookingRequest.getShowtimeId());
            booking.setTheaterId(bookingRequest.getTheaterId());
            booking.setMovieId(bookingRequest.getMovieId());
            booking.setSeatsBooked(bookingRequest.getNumberOfSeats());
            
            if (bookingRequest.getSeatNumbers() != null && !bookingRequest.getSeatNumbers().isEmpty()) {
                booking.setSeatNumbers(String.join(",", bookingRequest.getSeatNumbers()));
            }
            
            booking.setBasePrice(BigDecimal.valueOf(basePrice));
            booking.setDiscountApplied(BigDecimal.valueOf(discount));
            booking.setTotalPrice(BigDecimal.valueOf(totalPrice));
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setShowDateTime(LocalDateTime.parse(bookingRequest.getShowDateTime()));
            
            // 5. Save booking and update seat inventory
            Booking savedBooking = bookingRepo.save(booking);
            showtimeClient.bookSeats(bookingRequest.getShowtimeId(), bookingRequest.getNumberOfSeats());
            
            // 6. Prepare and return response
            return convertToResponseDTO(savedBooking, showtime);
            
        } catch (FeignException.NotFound e) {
            throw new ShowtimeNotFoundException("Showtime not found with id: " + bookingRequest.getShowtimeId());
        }
    }
    
    @Transactional
    public void cancelBooking(Long bookingId) {
        // Find the booking
        Booking booking = bookingRepo.findByIdAndIsActiveTrue(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Active booking not found with id: " + bookingId));
        
        // Validate if cancellation is allowed
        bookingValidator.validateBookingCancellation(booking.getBookingDate(), booking.getShowDateTime());
        
        // Check if already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED || !booking.getIsActive()) {
            throw new InvalidBookingException("Booking is already cancelled or inactive");
        }
        
        // Check if show has already started/ended
        if (booking.getShowDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidBookingException("Cannot cancel a booking for a show that has already started or ended");
        }
        
        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setIsActive(false);
        booking.setLastUpdated(LocalDateTime.now());
        
        // Save the updated booking
        bookingRepo.save(booking);
        
        // Update seat inventory (release the seats)
        try {
            int seatsToRelease = booking.getSeatNumbers().split(",").length;
            showtimeClient.updateSeatInventory(booking.getShowtimeId(), seatsToRelease);
            
            // Log successful cancellation
            // In a real application, you might want to send a notification to the user
            
        } catch (FeignException e) {
            // Log error but don't fail the cancellation
            // In a real application, consider implementing a retry mechanism or compensation transaction
            // You might want to implement a dead letter queue for failed seat inventory updates
            
            // For now, we'll just log the error
            // In a production environment, use a proper logging framework
            System.err.println("Failed to update seat inventory for cancelled booking: " + bookingId);
            System.err.println("Error: " + e.getMessage());
            
            // You might want to implement a retry mechanism here or use a message queue
            // to ensure the seat inventory gets updated eventually
        }
    }
    
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepo.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active booking not found with id: " + id));
        
        var showtime = showtimeClient.getShowtimeWithBookings(booking.getShowtimeId());
        return convertToResponseDTO(booking, showtime);
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepo.findByUserIdAndIsActiveTrue(userId).stream()
                .map(booking -> {
                    var showtime = showtimeClient.getShowtimeWithBookings(booking.getShowtimeId());
                    return convertToResponseDTO(booking, showtime);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByShowtimeId(Long showtimeId) {
        return bookingRepo.findByShowtimeIdAndIsActiveTrue(showtimeId).stream()
                .map(booking -> {
                    var showtime = showtimeClient.getShowtimeWithBookings(showtimeId);
                    return convertToResponseDTO(booking, showtime);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByTheaterAndDateRange(Long theaterId, String startDate, String endDate) {
        // Convert string dates to LocalDateTime and delegate to repository
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        
        return bookingRepo.findByTheaterIdAndShowDateTimeBetween(theaterId, start, end).stream()
                .map(booking -> {
                    var showtime = showtimeClient.getShowtimeWithBookings(booking.getShowtimeId());
                    return convertToResponseDTO(booking, showtime);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public double calculatePrice(Long showtimeId, int numTickets) {
        var showtime = showtimeClient.getShowtimeWithBookings(showtimeId);
        double basePrice = showtime.getPricePerSeat() * numTickets;
        double discount = calculateDiscount(showtime, numTickets);
        return Math.max(0, basePrice - discount);
    }
    
    @Override
    @Transactional
    public BulkBookingResponseDTO processBulkBooking(BulkBookingRequestDTO bulkRequest) {
        // Validate the bulk request
        //bulkRequest.();
        
        BulkBookingResponseDTO response = new BulkBookingResponseDTO();
        
        // Process each booking request in the bulk request
        bulkRequest.getBookings().forEach(bookingRequest -> {
            try {
                // Set the user ID from the bulk request if not set in individual request
                if (bookingRequest.getUserId() == null) {
                    bookingRequest.setUserId(bulkRequest.getUserId());
                }
                
                // Process the booking
                BookingResponseDTO bookingResponse = createBooking(bookingRequest);
                response.addSuccess(bookingRequest, bookingResponse);
                
            } catch (Exception e) {
                // Add the failed booking to the response
                response.addFailure(bookingRequest, e.getMessage());
                
                // In a real application, you might want to log the error or take other actions
                // For example, you could implement a retry mechanism for transient failures
            }
        });
        
        return response;
    }
    
    @Override
    @Transactional
    public int cancelMultipleBookings(List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) {
            return 0;
        }
        
        AtomicInteger successCount = new AtomicInteger(0);
        
        bookingIds.forEach(bookingId -> {
            try {
                // Cancel the booking
                cancelBooking(bookingId);
                successCount.incrementAndGet();
                
            } catch (Exception e) {
                // Log the error but continue with other cancellations
                System.err.println("Failed to cancel booking " + bookingId + ": " + e.getMessage());
                
                // In a real application, you might want to implement a retry mechanism
                // or add the failed IDs to a list to return to the caller
            }
        });
        
        return successCount.get();
    }
    
    private double calculateDiscount(ShowtimeDTO showtime, int numTickets) {
        double discount = 0;
        
        // 50% discount on the 3rd ticket
        if (numTickets >= 3) {
            discount += showtime.getPricePerSeat() * 0.5;
        }
        
        // 20% discount for afternoon shows (12 PM - 5 PM)
        if (showtime.isAfternoonShow()) {
            discount += (showtime.getPricePerSeat() * numTickets) * 0.2;
        }
        
        return discount;
    }
    
    private BookingResponseDTO convertToResponseDTO(Booking booking, ShowtimeDTO showtime) {
        BookingResponseDTO dto = modelMapper.map(booking, BookingResponseDTO.class);
        dto.setTheaterName(showtime.getTheaterName());
        dto.setCity(showtime.getCity());
        dto.setAfternoonShow(showtime.isAfternoonShow());
        
        // Parse seat numbers if they exist
        if (booking.getSeatNumbers() != null && !booking.getSeatNumbers().isEmpty()) {
            dto.setSeatNumbers(Arrays.asList(booking.getSeatNumbers().split(",")));
        }
        
        return dto;
    }
}
