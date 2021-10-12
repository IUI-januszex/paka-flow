package pl.com.januszex.paka.flow.configuration;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {
    LocalDateTime timestamp;
    String message;
}
