package pl.com.januszex.paka.flow.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import pl.com.januszex.paka.flow.base.exception.BusinessLogicException;
import pl.com.januszex.paka.flow.base.exception.NotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionExceptionHandler(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause().getCause();
        ErrorResponse errors;
        if (cause instanceof IllegalArgumentException) {
            errors = new ErrorResponse(LocalDateTime.now(), cause.getMessage());
        } else {
            errors = new ErrorResponse(LocalDateTime.now(), "Invalid input");
            Writer buffer = new StringWriter();
            PrintWriter pw = new PrintWriter(buffer);
            ex.printStackTrace(pw);
            log.warn(buffer.toString());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(NotFoundException ex) {
        ErrorResponse errors = new ErrorResponse(LocalDateTime.now(), ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> businessLogicExceptionHandler(BusinessLogicException ex) {
        ErrorResponse errors = new ErrorResponse(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errors = new ErrorResponse(LocalDateTime.now(), ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> radiationException(MethodArgumentNotValidException ex) {
        String errorMessage = getFieldErrorMessages(ex);
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),
                errorMessage),
                HttpStatus.BAD_REQUEST);
    }

    private String getFieldErrorMessages(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(". ", "", "."));
    }
}
