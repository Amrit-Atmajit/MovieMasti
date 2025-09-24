package com.moviemasti.bookings.dtos;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;
    
    @NotNull(message = "Theater ID is required")
    private Long theaterId;
    
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    
    @NotNull(message = "Number of seats is required")
    @Min(value = 1, message = "At least one seat must be booked")
    private Integer numberOfSeats;
    
    private List<String> seatNumbers; // Optional: Specific seat numbers if applicable
    
    @NotNull(message = "Show date and time is required")
    private String showDateTime;
}
