package com.moviemasti.bookings.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moviemasti.bookings.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOKINGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "showtime_id", nullable = false)
    private Long showtimeId;
    
    @Column(name = "theater_id", nullable = false)
    private Long theaterId;
    
    @Column(name = "movie_id", nullable = false)
    private Long movieId;
    
    @Column(name = "seats_booked", nullable = false)
    private Integer seatsBooked;
    
    @Column(name = "seat_numbers", columnDefinition = "TEXT")
    private String seatNumbers;  // Comma-separated seat numbers

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;
    
    @Column(name = "discount_applied", precision = 10, scale = 2)
    private BigDecimal discountApplied = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;
    
    @Column(name = "show_date_time", nullable = false)
    private LocalDateTime showDateTime;
    
    @Column(name = "booking_date", nullable = false, updatable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;
    
    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
