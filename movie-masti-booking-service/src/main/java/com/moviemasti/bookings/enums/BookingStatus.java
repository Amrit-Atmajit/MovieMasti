package com.moviemasti.bookings.enums;

public enum BookingStatus {
    PENDING,        // Booking is being processed
    CONFIRMED,      // Booking is confirmed and seats are reserved
    CANCELLED,      // Booking was cancelled
    COMPLETED,      // Show has been watched
    EXPIRED        // Booking expired due to non-payment or other reasons
}
