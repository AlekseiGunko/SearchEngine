package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import searchengine.dto.statistics.exception.ErrorMessage;

@RestControllerAdvice
public class ApiErrorController {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> nullPointerException(NullPointerException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("Данные поискового запроса отсутствуют в базе сервера: "
                        + exception.getMessage()));
    }

}
