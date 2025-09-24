package com.moviemasti.bookings.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;
    private Long userId;
    private Long showtimeId;
    private Integer seatsBooked;

}
