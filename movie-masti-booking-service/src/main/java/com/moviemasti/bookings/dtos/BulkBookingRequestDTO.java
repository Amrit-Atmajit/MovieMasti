package com.moviemasti.bookings.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.validation.Valid;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for bulk booking requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkBookingRequestDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotEmpty(message = "At least one booking is required")
    @Valid
    private List<BookingRequestDTO> bookings;

    @AssertTrue(message = "All bookings must be for the same user as specified in the bulk request")
    private boolean isValidUserConsistency() {
        if (bookings == null || bookings.isEmpty()) {
            return false;
        }
        return bookings.stream().allMatch(booking -> userId.equals(booking.getUserId()));
    }

    /**
     * Validates that all bookings in the bulk request are for the same user
     */
    /*public void validate() {
        if (bookings == null || bookings.isEmpty()) {
            throw new IllegalArgumentException("At least one booking is required");
        }
        
        // Ensure all bookings are for the same user as specified in the bulk request
        boolean allMatchUserId = bookings.stream()
                .allMatch(booking -> booking.getUserId().equals(userId));
                
        if (!allMatchUserId) {
            throw new IllegalArgumentException("All bookings must be for the same user as specified in the bulk request");
        }
        
        // Validate each booking request
        bookings.forEach(BookingRequestDTO::validate);
    }*/
}
