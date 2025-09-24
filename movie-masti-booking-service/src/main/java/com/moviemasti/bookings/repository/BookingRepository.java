package com.moviemasti.bookings.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moviemasti.bookings.entity.Booking;
import com.moviemasti.bookings.enums.BookingStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByShowtimeId(Long showtimeId);
    
    List<Booking> findByUserIdAndIsActiveTrue(Long userId);
    
    List<Booking> findByShowtimeIdAndIsActiveTrue(Long showtimeId);
    
    Optional<Booking> findByIdAndIsActiveTrue(Long id);
    
    List<Booking> findByTheaterIdAndShowDateTimeBetween(Long theaterId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "b.userId = :userId AND " +
           "b.showDateTime >= CURRENT_TIMESTAMP AND " +
           "b.status = :status AND " +
           "b.isActive = true")
    List<Booking> findUpcomingBookingsByUserAndStatus(
            @Param("userId") Long userId,
            @Param("status") BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "b.theaterId = :theaterId AND " +
           "b.showDateTime BETWEEN :startDate AND :endDate AND " +
           "b.isActive = true")
    List<Booking> findByTheaterAndDateRange(
            @Param("theaterId") Long theaterId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE " +
           "b.userId = :userId AND " +
           "b.showtimeId = :showtimeId AND " +
           "b.status = 'CONFIRMED' AND " +
           "b.isActive = true")
    boolean existsActiveBookingForUserAndShowtime(
            @Param("userId") Long userId,
            @Param("showtimeId") Long showtimeId);
    
    default Booking findByIdAndIsActiveTrueOrThrow(Long id) {
        return findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new com.moviemasti.bookings.exception.ResourceNotFoundException("Booking not found with id: " + id));
    }
}
