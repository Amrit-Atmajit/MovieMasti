package com.moviemasti.bookings.controller;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.moviemasti.bookings.dtos.BookingRequestDTO;
//import com.moviemasti.bookings.dtos.BookingResponseDTO;
//import com.moviemasti.bookings.dtos.BulkBookingRequestDTO;
//import com.moviemasti.bookings.dtos.BulkBookingResponseDTO;
//import com.moviemasti.bookings.entity.Booking;
//import com.moviemasti.bookings.enums.BookingStatus;
//import com.moviemasti.bookings.service.BookingService;
//import com.moviemasti.showtime.dtos.ShowtimeDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(BookingController.class)
//@ActiveProfiles("test")
public class BookingControllerIntegrationTest {

    /*@Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingRequestDTO bookingRequestDTO;
    private BookingResponseDTO bookingResponseDTO;
    private ShowtimeDTO showtimeDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setUserId(1L);
        bookingRequestDTO.setShowtimeId(1L);
        bookingRequestDTO.setTheaterId(1L);
        bookingRequestDTO.setMovieId(1L);
        bookingRequestDTO.setSeatNumbers(Collections.singletonList("A1"));
        bookingRequestDTO.setShowDateTime(LocalDateTime.now().plusDays(1));

        bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setId(1L);
        bookingResponseDTO.setUserId(1L);
        bookingResponseDTO.setStatus(BookingStatus.CONFIRMED);
        bookingResponseDTO.setTotalPrice(200.0);

        showtimeDTO = new ShowtimeDTO();
        showtimeDTO.setId(1L);
        showtimeDTO.setTheaterId(1L);
        showtimeDTO.setMovieId(1L);
        showtimeDTO.setShowDate(LocalDate.now().plusDays(1));
        showtimeDTO.setShowTime(LocalTime.of(14, 0));
        showtimeDTO.setPricePerSeat(200.0);
        showtimeDTO.setTotalSeats(100);
        showtimeDTO.setAvailableSeats(99);
        showtimeDTO.setActive(true);
    }

    @Test
    void createBooking_ValidRequest_ReturnsCreated() throws Exception {
        when(bookingService.createBooking(any(BookingRequestDTO.class))).thenReturn(bookingResponseDTO);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void getBookingById_ExistingId_ReturnsBooking() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(bookingResponseDTO);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void cancelBooking_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBulkBookings_ValidRequest_ReturnsMultiStatus() throws Exception {
        BulkBookingRequestDTO bulkRequest = new BulkBookingRequestDTO();
        bulkRequest.setUserId(1L);
        bulkRequest.setBookings(Arrays.asList(bookingRequestDTO, bookingRequestDTO));

        BulkBookingResponseDTO bulkResponse = new BulkBookingResponseDTO();
        bulkResponse.setTotalRequests(2);
        bulkResponse.setSuccessfulBookings(2);
        bulkResponse.setFailedBookings(0);

        when(bookingService.processBulkBooking(any(BulkBookingRequestDTO.class))).thenReturn(bulkResponse);

        mockMvc.perform(post("/api/bookings/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bulkRequest)))
                .andExpect(status().isMultiStatus())
                .andExpect(jsonPath("$.totalRequests").value(2))
                .andExpect(jsonPath("$.successfulBookings").value(2))
                .andExpect(jsonPath("$.failedBookings").value(0));
    }

    @Test
    void cancelMultipleBookings_ValidIds_ReturnsOk() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("totalRequested", 3);
        response.put("successfullyCancelled", 3);
        response.put("failed", 0);

        when(bookingService.cancelMultipleBookings(anyList())).thenReturn(3);

        mockMvc.perform(delete("/api/bookings/bulk")
                .param("bookingIds", "1", "2", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRequested").value(3))
                .andExpect(jsonPath("$.successfullyCancelled").value(3))
                .andExpect(jsonPath("$.failed").value(0));
    }

    @Test
    void calculatePrice_ValidInput_ReturnsPrice() throws Exception {
        when(bookingService.calculatePrice(1L, 3)).thenReturn(380.0);

        mockMvc.perform(get("/api/bookings/calculate-price")
                .param("showtimeId", "1")
                .param("numTickets", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("380.0"));
    }*/
}
