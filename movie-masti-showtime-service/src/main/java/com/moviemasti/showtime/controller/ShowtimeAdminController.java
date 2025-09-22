package com.moviemasti.showtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviemasti.showtime.entity.Showtime;
import com.moviemasti.showtime.service.ShowtimeAdminService;

@RestController
@RequestMapping("/api/showtimes/admin")
public class ShowtimeAdminController {
	
	@Autowired
    private ShowtimeAdminService showtimeAdminService;
	
	@PostMapping
    public Showtime createShowtime(@RequestBody Showtime showtime) {
        return showtimeAdminService.createShowtime(showtime);
    }
}
