package learn.field_agent.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 2
    public ResponseEntity<String> handleException(Exception ex) { // 3
        return new ResponseEntity<String>(
                "Something went wrong on our end. Your request failed.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
