package com.moviemasti.bookings.exception;

public class ShowtimeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ShowtimeNotFoundException(String message) {
        super(message);
    }
}
