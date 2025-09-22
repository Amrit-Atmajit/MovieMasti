package com.moviemasti.showtime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviemasti.showtime.dtos.BookingDTO;
import com.moviemasti.showtime.dtos.ShowtimeDTO;
import com.moviemasti.showtime.entity.Showtime;
import com.moviemasti.showtime.repository.ShowtimeRepository;

@Service
public class ShowtimeService {
	
	@Autowired
	private ShowtimeRepository showtimeRepo;
	
	@Autowired
	private BookingServiceClient bookingClient;
	
	public List<Showtime> getAllShowtimes() {
		return showtimeRepo.findAll();
	}
	
	public ShowtimeDTO getShowtimeWithBookings(Long id) {
        Showtime showtime = showtimeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<BookingDTO> bookings = bookingClient.getBookingsByShowtimeId(id);
        
        ShowtimeDTO showtimeDTO = mapToDTO(showtime);
        showtimeDTO.setBookings(bookings);

        return showtimeDTO;
    }
	
	public Showtime bookSeats(Long showtimeId, Integer seats) {
		Showtime showtime = showtimeRepo.findById(showtimeId).orElseThrow(() -> new RuntimeException("Showtime not found"));
		
		if (showtime.getAvailableSeats() < seats) {
    		throw new RuntimeException("Not enough seats available");
    	}
    	
    	showtime.setAvailableSeats(showtime.getAvailableSeats() - seats);
    	
    	return showtimeRepo.save(showtime);
	}
	
	public Boolean doesShowtimeExist(Long id) {
		return showtimeRepo.existsById(id);
	}
	
	private ShowtimeDTO mapToDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());
        dto.setMovieId(showtime.getMovieId());
        dto.setShowTime(showtime.getShowTime());
        dto.setAvailableSeats(showtime.getAvailableSeats());
        return dto;
    }
}
