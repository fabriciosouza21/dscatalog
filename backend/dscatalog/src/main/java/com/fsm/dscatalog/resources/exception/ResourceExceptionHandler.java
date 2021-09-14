package com.fsm.dscatalog.resources.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fsm.dscatalog.services.exception.NotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound (NotFoundException e , HttpServletRequest request){
		StandardError err = new StandardError();
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setMessage(e.getMessage());
		err.setTimestamp(Instant.now());
		err.setPath(request.getRequestURI());
		err.setError("resource not found");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
