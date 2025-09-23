package com.moviemasti.showtime.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDTO {
    private Long id;
    private Long movieId;
    private LocalDateTime showTime;
    private Integer availableSeats;
    private List<BookingDTO> bookings;

}
