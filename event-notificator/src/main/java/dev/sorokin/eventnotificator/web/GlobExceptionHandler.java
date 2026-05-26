package dev.sorokin.eventnotificator.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
                ? constructMethodArgumentNotValidMessage((MethodArgumentNotValidException) e)
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ServerErrorDto> handleUnauthorized(UnauthorizedException e) {
        var errorDto = new ServerErrorDto(
                "У данного пользователя нет прав на чтение уведомлений",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorDto);
    }

    private static String constructMethodArgumentNotValidMessage(
            MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
