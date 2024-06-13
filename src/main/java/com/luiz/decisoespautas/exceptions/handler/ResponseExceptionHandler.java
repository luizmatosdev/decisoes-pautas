package com.luiz.decisoespautas.exceptions.handler;

import com.luiz.decisoespautas.exceptions.ExceptionResponse;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateRequestException(DuplicateRequestException e, HttpServletRequest request) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleException(IllegalArgumentException e) {
        ExceptionResponse exception = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
