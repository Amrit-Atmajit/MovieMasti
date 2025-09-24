package com.moviemasti.showtime.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviemasti.showtime.dtos.BookingDTO;
import com.moviemasti.showtime.dtos.ShowtimeDTO;
import com.moviemasti.showtime.entity.Showtime;
import com.moviemasti.showtime.exception.ResourceNotFoundException;
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
    
    public Showtime getShowtimeById(Long id) {
        return showtimeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
    }
	
    @Transactional(readOnly = true)
    public List<ShowtimeDTO> findShowsByMovieAndCityAndDate(Long movieId, String city, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        return showtimeRepo.findByMovieIdAndCityAndShowDateBetweenAndIsActiveTrue(
                movieId, city, startOfDay, endOfDay)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ShowtimeDTO> findShowsByTheaterAndDate(Long theaterId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        return showtimeRepo.findByTheaterIdAndShowDateBetweenAndIsActiveTrue(
                theaterId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Showtime createShowtime(ShowtimeDTO showtimeDTO) {
        Showtime showtime = new Showtime();
        updateShowtimeFromDTO(showtime, showtimeDTO);
        return showtimeRepo.save(showtime);
    }
    
    @Transactional
    public Showtime updateShowtime(Long id, ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
        updateShowtimeFromDTO(showtime, showtimeDTO);
        return showtimeRepo.save(showtime);
    }
    
    @Transactional
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
        showtime.setIsActive(false);
        showtimeRepo.save(showtime);
    }
    
    @Transactional
    public void updateSeatInventory(Long showtimeId, int seatAdjustment) {
        Showtime showtime = showtimeRepo.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));
        
        int newAvailableSeats = showtime.getAvailableSeats() + seatAdjustment;
        if (newAvailableSeats < 0 || newAvailableSeats > showtime.getTotalSeats()) {
            throw new IllegalStateException("Invalid seat adjustment. Available seats would be: " + newAvailableSeats);
        }
        
        showtime.setAvailableSeats(newAvailableSeats);
        showtimeRepo.save(showtime);
    }
    
    public double calculateTotalPrice(Showtime showtime, int numTickets) {
        double basePrice = showtime.getPricePerSeat() * numTickets;
        
        // Apply 50% discount on the 3rd ticket
        if (numTickets >= 3) {
            double discount = showtime.getPricePerSeat() * 0.5;
            basePrice -= discount;
        }
        
        // Apply 20% discount for afternoon shows
        if (showtime.isAfternoonShow()) {
            basePrice *= 0.8;
        }
        
        return Math.max(0, basePrice); // Ensure price is not negative
    }
    
    private void updateShowtimeFromDTO(Showtime showtime, ShowtimeDTO dto) {
        showtime.setMovieId(dto.getMovieId());
        showtime.setTheaterId(dto.getTheaterId());
        showtime.setTheaterName(dto.getTheaterName());
        showtime.setCity(dto.getCity());
        showtime.setShowDate(dto.getShowDate());
        showtime.setShowTime(dto.getShowTime());
        showtime.setAvailableSeats(dto.getAvailableSeats());
        showtime.setTotalSeats(dto.getTotalSeats());
        showtime.setPricePerSeat(dto.getPricePerSeat());
        showtime.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
    }
    
    private ShowtimeDTO mapToDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());
        dto.setMovieId(showtime.getMovieId());
        dto.setTheaterId(showtime.getTheaterId());
        dto.setTheaterName(showtime.getTheaterName());
        dto.setCity(showtime.getCity());
        dto.setShowDate(showtime.getShowDate());
        dto.setShowTime(showtime.getShowTime());
        dto.setAvailableSeats(showtime.getAvailableSeats());
        dto.setTotalSeats(showtime.getTotalSeats());
        dto.setPricePerSeat(showtime.getPricePerSeat());
        dto.setIsActive(showtime.getIsActive());
        return dto;
    }
}
