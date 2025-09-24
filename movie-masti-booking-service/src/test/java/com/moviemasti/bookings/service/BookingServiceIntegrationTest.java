package com.moviemasti.bookings.service;

/*import com.moviemasti.bookings.dtos.BookingRequestDTO;
import com.moviemasti.bookings.dtos.BookingResponseDTO;
import com.moviemasti.bookings.dtos.BulkBookingRequestDTO;
import com.moviemasti.bookings.entity.Booking;
import com.moviemasti.bookings.enums.BookingStatus;
import com.moviemasti.bookings.exception.InvalidBookingException;
import com.moviemasti.bookings.exception.ResourceNotFoundException;
import com.moviemasti.bookings.exception.ShowtimeNotFoundException;
import com.moviemasti.bookings.repository.BookingRepository;
import com.moviemasti.bookings.util.ModelMapper;
import com.moviemasti.bookings.validation.BookingValidator;
import com.moviemasti.showtime.dtos.ShowtimeDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")*/
public class BookingServiceIntegrationTest {

    /*@Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowtimeClient showtimeClient;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BookingValidator bookingValidator;

    @InjectMocks
    private BookingService bookingService;

    private BookingRequestDTO bookingRequestDTO;
    private Booking booking;
    private ShowtimeDTO showtimeDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setUserId(1L);
        bookingRequestDTO.setShowtimeId(1L);
        bookingRequestDTO.setTheaterId(1L);
        bookingRequestDTO.setMovieId(1L);
        bookingRequestDTO.setSeatNumbers(Arrays.asList("A1", "A2"));
        bookingRequestDTO.setShowDateTime(LocalDateTime.now().plusDays(1));

        booking = new Booking();
        booking.setId(1L);
        booking.setUserId(1L);
        booking.setShowtimeId(1L);
        booking.setTheaterId(1L);
        booking.setMovieId(1L);
        booking.setSeatNumbers("A1,A2");
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setActive(true);

        showtimeDTO = new ShowtimeDTO();
        showtimeDTO.setId(1L);
        showtimeDTO.setTheaterId(1L);
        showtimeDTO.setMovieId(1L);
        showtimeDTO.setShowDate(LocalDate.now().plusDays(1));
        showtimeDTO.setShowTime(LocalTime.of(14, 0)); // 2:00 PM
        showtimeDTO.setPricePerSeat(200.0);
        showtimeDTO.setTotalSeats(100);
        showtimeDTO.setAvailableSeats(98);
        showtimeDTO.setActive(true);
    }

    @Test
    void createBooking_ValidRequest_ReturnsBookingResponse() {
        // Arrange
        when(showtimeClient.doesShowtimeExist(anyLong())).thenReturn(true);
        when(showtimeClient.getShowtimeWithBookings(anyLong())).thenReturn(showtimeDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(modelMapper.map(any(Booking.class), any())).thenReturn(new BookingResponseDTO());

        // Act
        BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO);

        // Assert
        assertNotNull(response);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(showtimeClient, times(1)).updateSeatInventory(anyLong(), anyInt());
    }

    @Test
    void createBooking_ShowtimeNotFound_ThrowsException() {
        // Arrange
        when(showtimeClient.doesShowtimeExist(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ShowtimeNotFoundException.class, 
            () -> bookingService.createBooking(bookingRequestDTO));
    }

    @Test
    void cancelBooking_ValidId_CancelsBooking() {
        // Arrange
        when(bookingRepository.findByIdAndIsActiveTrue(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        bookingService.cancelBooking(1L);

        // Assert
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertFalse(booking.isActive());
        verify(bookingRepository, times(1)).save(booking);
        verify(showtimeClient, times(1)).updateSeatInventory(anyLong(), anyInt());
    }

    @Test
    void cancelBooking_BookingNotFound_ThrowsException() {
        // Arrange
        when(bookingRepository.findByIdAndIsActiveTrue(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> bookingService.cancelBooking(1L));
    }

    @Test
    void processBulkBooking_ValidRequest_ProcessesAllBookings() {
        // Arrange
        BulkBookingRequestDTO bulkRequest = new BulkBookingRequestDTO();
        bulkRequest.setUserId(1L);
        bulkRequest.setBookings(Arrays.asList(bookingRequestDTO, bookingRequestDTO));

        when(showtimeClient.doesShowtimeExist(anyLong())).thenReturn(true);
        when(showtimeClient.getShowtimeWithBookings(anyLong())).thenReturn(showtimeDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(modelMapper.map(any(Booking.class), any())).thenReturn(new BookingResponseDTO());

        // Act
        var response = bookingService.processBulkBooking(bulkRequest);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTotalRequests());
        assertEquals(2, response.getSuccessfulBookings());
        assertEquals(0, response.getFailedBookings());
    }

    @Test
    void cancelMultipleBookings_ValidIds_CancelsAllBookings() {
        // Arrange
        List<Long> bookingIds = Arrays.asList(1L, 2L, 3L);
        when(bookingRepository.findByIdAndIsActiveTrue(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Booking b = new Booking();
            b.setId(id);
            b.setStatus(BookingStatus.CONFIRMED);
            b.setActive(true);
            b.setShowtimeId(1L);
            b.setSeatNumbers("A1");
            return Optional.of(b);
        });

        // Act
        int cancelledCount = bookingService.cancelMultipleBookings(bookingIds);

        // Assert
        assertEquals(3, cancelledCount);
        verify(bookingRepository, times(3)).save(any(Booking.class));
    }

    @Test
    void calculatePrice_AfternoonShow_AppliesDiscount() {
        // Arrange
        showtimeDTO.setShowTime(LocalTime.of(14, 0)); // 2:00 PM - afternoon show
        when(showtimeClient.getShowtimeWithBookings(anyLong())).thenReturn(showtimeDTO);

        // Act
        double price = bookingService.calculatePrice(1L, 3);

        // Assert
        // Base price: 3 * 200 = 600
        // Afternoon discount (20%): 120
        // 3rd ticket discount (50% of 200): 100
        // Expected: 600 - 120 - 100 = 380
        assertEquals(380.0, price, 0.01);
    }*/
}
