package com.tutorialspoint.demo.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class BookExceptionController {
	@ExceptionHandler(value = BookNotfoundException.class)
	public ResponseEntity<Object> exception(BookNotfoundException exception) {
		//aqui hago todo lo que tenga que hacer con la excepcion...
		System.out.println(exception.getMessage());
		return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
	}
}