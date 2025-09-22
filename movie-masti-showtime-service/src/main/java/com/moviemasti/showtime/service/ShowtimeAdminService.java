package com.moviemasti.showtime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviemasti.showtime.entity.Showtime;
import com.moviemasti.showtime.exception.MovieNotFoundException;
import com.moviemasti.showtime.repository.ShowtimeRepository;

@Service
public class ShowtimeAdminService {
	@Autowired
    private ShowtimeRepository showtimeRepo;
	
	@Autowired
    private MovieServiceClient movieClient;
	
	public Showtime createShowtime(Showtime showtime) {
        if (!movieClient.validateMovieId(showtime.getMovieId())) {
            throw new MovieNotFoundException("Movie not found for ID: " + showtime.getMovieId());
        }
        return showtimeRepo.save(showtime);
    }
}
