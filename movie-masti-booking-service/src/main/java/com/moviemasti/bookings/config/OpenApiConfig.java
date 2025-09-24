package com.moviemasti.bookings.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Configuration for OpenAPI (Swagger) documentation
 */
//@Configuration
public class OpenApiConfig {

    //@Value("${server.port:8080}")
    private String serverPort;
    
    //@Value("${spring.application.name:booking-service}")
    private String serviceName;
    
    //@Bean
    //@Primary
    public OpenAPI customOpenAPI() {
        String serviceUrl = String.format("http://localhost:%s/%s", serverPort, serviceName);
        
        return new OpenAPI()
                .servers(List.of(new Server().url(serviceUrl)));
                /*.info(new Info()
                        .title("Movie Masti " + serviceName + " API")
                        .description("""
                                ## Movie Masti "" + serviceName + """

                                This is the OpenAPI documentation for the "" + serviceName + """ microservice.

                                ### API Endpoints
                                - `/api/bookings` - Booking management
                                - `/api/bookings/bulk` - Bulk booking operations
                                - `/api/bookings/user/{userId}` - Get user bookings
                                - `/api/bookings/showtime/{showtimeId}` - Get showtime bookings

                                ### Authentication
                                All endpoints require a valid JWT token in the `Authorization` header.

                                Example: `Authorization: Bearer your.jwt.token.here`"""
                                
                                ### Error Responses
                                - `401 Unauthorized` - Missing or invalid authentication
                                - `403 Forbidden` - Insufficient permissions
                                - `404 Not Found` - Resource not found
                                - `400 Bad Request` - Invalid request data
                                - `500 Internal Server Error` - Server error
                                
                                For more information, please contact the development team.""")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Movie Masti Support")
                                .email("support@moviemasti.com")
                                .url("https://www.moviemasti.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));*/
    }
}
