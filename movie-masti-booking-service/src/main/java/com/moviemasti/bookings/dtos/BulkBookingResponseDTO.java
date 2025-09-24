package com.moviemasti.bookings.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for bulk booking responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkBookingResponseDTO {
    
    private int totalRequests;
    private int successfulBookings;
    private int failedBookings;
    private List<BulkBookingResultDTO> results = new ArrayList<>();
    
    /**
     * Add a successful booking result
     * @param request The original booking request
     * @param response The successful booking response
     */
    public void addSuccess(BookingRequestDTO request, BookingResponseDTO response) {
        results.add(new BulkBookingResultDTO(
                request,
                response,
                true,
                "Booking successful"
        ));
        successfulBookings++;
        totalRequests++;
    }
    
    /**
     * Add a failed booking result
     * @param request The original booking request
     * @param errorMessage The error message
     */
    public void addFailure(BookingRequestDTO request, String errorMessage) {
        results.add(new BulkBookingResultDTO(
                request,
                null,
                false,
                errorMessage
        ));
        failedBookings++;
        totalRequests++;
    }
    
    /**
     * Inner class for individual booking results
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkBookingResultDTO {
        private BookingRequestDTO request;
        private BookingResponseDTO response;
        private boolean success;
        private String message;
    }
}
