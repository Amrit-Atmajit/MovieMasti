package com.moviemasti.bookings.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviemasti.bookings.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private Long showtimeId;
    private Long theaterId;
    private Long movieId;
    private Integer seatsBooked;
    private List<String> seatNumbers;
    private BigDecimal totalPrice;
    private BigDecimal basePrice;
    private BigDecimal discountApplied;
    private BookingStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;
    
    private String theaterName;
    private String movieTitle;
    private String city;
    private boolean isAfternoonShow;
}
