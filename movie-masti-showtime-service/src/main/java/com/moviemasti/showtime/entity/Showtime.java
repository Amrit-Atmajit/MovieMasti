package com.moviemasti.showtime.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
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
	
	@Column(name = "show_time", nullable = false)
	private LocalDateTime showTime;
	
	@Column(name = "available_seats", nullable = false)
	private Integer availableSeats;

}
