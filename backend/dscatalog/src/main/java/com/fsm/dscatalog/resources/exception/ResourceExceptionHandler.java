package com.fsm.dscatalog.resources.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fsm.dscatalog.services.exception.DatabaseException;
import com.fsm.dscatalog.services.exception.NotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound (NotFoundException e , HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus status = HttpStatus.NOT_FOUND;		
		
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setTimestamp(Instant.now());
		err.setPath(request.getRequestURI());
		err.setError("resource not found");
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database (DatabaseException e , HttpServletRequest request){
		StandardError err = new StandardError();
		HttpStatus status = HttpStatus.BAD_REQUEST;		
		
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setTimestamp(Instant.now());
		err.setPath(request.getRequestURI());
		err.setError("database exception");
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidateError> notValition(MethodArgumentNotValidException e , HttpServletRequest request){
		ValidateError err = new ValidateError();
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;		
		err.setStatus(status.value());
		err.setMessage(e.getMessage());
		err.setTimestamp(Instant.now());
		err.setPath(request.getRequestURI());
		err.setError("validate exception");
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addFieldMessage(f.getField(), f.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(err);
	}
	
}
