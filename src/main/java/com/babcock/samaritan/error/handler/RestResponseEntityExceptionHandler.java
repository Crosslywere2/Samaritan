package com.babcock.samaritan.error.handler;

import com.babcock.samaritan.error.InvalidItemOwnerException;
import com.babcock.samaritan.error.InvalidLoginCredentialsException;
import com.babcock.samaritan.error.ItemNotFoundException;
import com.babcock.samaritan.error.UserNotFoundException;
import com.babcock.samaritan.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public ResponseEntity<ErrorMessage> invalidLoginCredentialsException(InvalidLoginCredentialsException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(message.getStatus()).body(message);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(message.getStatus()).body(message);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorMessage> itemNotFoundException(ItemNotFoundException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(message.getStatus()).body(message);
    }

    @ExceptionHandler(InvalidItemOwnerException.class)
    public ResponseEntity<ErrorMessage> invalidItemOwnerException(InvalidItemOwnerException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(message.getStatus()).body(message);
    }
}
