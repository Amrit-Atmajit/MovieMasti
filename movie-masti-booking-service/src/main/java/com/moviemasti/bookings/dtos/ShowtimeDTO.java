package com.moviemasti.bookings.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDTO {
    private Long id;
    private Long movieId;
    private Long theaterId;
    private String theaterName;
    private String city;
    private LocalDateTime showDate;
    private LocalTime showTime;
    private Integer availableSeats;
    private Integer totalSeats;
    private Double pricePerSeat;
    private Boolean isActive;
    private List<BookingDTO> bookings;
    
    public boolean isAfternoonShow() {
        if (showTime == null) return false;
        LocalTime afternoonStart = LocalTime.of(12, 0);
        LocalTime afternoonEnd = LocalTime.of(17, 0);
        return !showTime.isBefore(afternoonStart) && showTime.isBefore(afternoonEnd);
    }
}
