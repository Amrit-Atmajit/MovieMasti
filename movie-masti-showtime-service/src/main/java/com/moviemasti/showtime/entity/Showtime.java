package com.moviemasti.showtime.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SHOWTIMES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "movie_id", nullable = false)
    private Long movieId;
    
    @Column(name = "theater_id", nullable = false)
    private Long theaterId;
    
    @Column(name = "theater_name", nullable = false)
    private String theaterName;
    
    @Column(name = "city", nullable = false)
    private String city;
    
    @Column(name = "show_date", nullable = false)
    private LocalDateTime showDate;
    
    @Column(name = "show_time", nullable = false)
    private LocalTime showTime;
    
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
    
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @Column(name = "price_per_seat", nullable = false)
    private Double pricePerSeat;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;
    
    public boolean isAfternoonShow() {
        LocalTime afternoonStart = LocalTime.of(12, 0);
        LocalTime afternoonEnd = LocalTime.of(17, 0);
        return !showTime.isBefore(afternoonStart) && showTime.isBefore(afternoonEnd);
    }
}
