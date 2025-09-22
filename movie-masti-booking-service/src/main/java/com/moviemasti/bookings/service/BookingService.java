package com.moviemasti.bookings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviemasti.bookings.entity.Booking;
import com.moviemasti.bookings.exception.ShowtimeNotFoundException;
import com.moviemasti.bookings.repository.BookingRepository;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private ShowtimeClient showtimeClient;
	
	public Booking bookTickets(Long userId, Long showtimeId, Integer seats) {
		if (!showtimeClient.doesShowtimeExist(showtimeId)) {
			throw new ShowtimeNotFoundException("Invalid showtimeId: Showtime does not exist");
		}
		
		showtimeClient.bookSeats(showtimeId, seats);
		
		Booking booking = new Booking();
		booking.setUserId(userId);
		booking.setShowtimeId(showtimeId);
		booking.setSeatsBooked(seats);
		
		return bookingRepo.save(booking);
	}
	
	public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepo.findByUserId(userId);
    }
	
	public List<Booking> getBookingByShowtimeId(Long showtimeId){
		return bookingRepo.findByShowtimeId(showtimeId);
	}
}
