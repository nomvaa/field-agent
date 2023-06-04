package learn.field_agent.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<String>(
                "Something went wrong on our end. Your request failed.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementException(NoSuchElementException ex){
        return new ResponseEntity<String>("Request does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException ex){
        return new ResponseEntity<String>("Method used has failed our validations.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> illegalAccessException(IllegalAccessException ex){
        return new ResponseEntity<String>("That's an illegal move... Not allowed here.", HttpStatus.BAD_REQUEST);
    }


}
