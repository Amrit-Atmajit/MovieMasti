package com.moviemasti.bookings.util;

import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

/**
 * Custom ModelMapper configuration for mapping between entities and DTOs
 */
@Component
public class BookingModelMapper extends org.modelmapper.ModelMapper {
    
    public BookingModelMapper() {
        super();
        configureForResponse();
    }
    
    private void configureForResponse() {
        Configuration configuration = getConfiguration();
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
        configuration.setSkipNullEnabled(true);
        
        // Add any custom type mappings here if needed
    }
    
    /**
     * Maps the source object to an instance of the destination class
     * @param source the source object to map from
     * @param destinationClass the destination class to map to
     * @param <D> the type of the destination object
     * @return a new instance of the destination class with values from the source object
     */
    public <D> D map(Object source, Class<D> destinationClass) {
        if (source == null) {
            return null;
        }
        return super.map(source, destinationClass);
    }
}
