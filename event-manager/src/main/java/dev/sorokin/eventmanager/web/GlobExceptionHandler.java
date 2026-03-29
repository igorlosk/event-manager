package dev.sorokin.eventmanager.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobExceptionHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ServerErrorDto> handleValidationException(Exception e) {
        LOGGER.error("Got validation exception" + e);

        String detailedMessage = e instanceof MethodArgumentNotValidException
                ? constractMethodArgumentNotValidMessage((MethodArgumentNotValidException) e)
                : e.getMessage();
        var newDto = new ServerErrorDto(
                "Некорректный запрос",
                detailedMessage,
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(newDto);
    }


    @ExceptionHandler()
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception e) {
        LOGGER.error("Server error" + e);
        var newDto = new ServerErrorDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(newDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handlerNotFoundException(EntityNotFoundException e) {
        LOGGER.error("Got exception" + e);
        var newDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(newDto);
    }

    private static String constractMethodArgumentNotValidMessage(
            MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
