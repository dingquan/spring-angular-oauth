package com.example.testapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT) 
public class EntityAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1071099991358231691L;
	
	public EntityAlreadyExistsException(String msg) {
		super(msg);
	}
	
	public EntityAlreadyExistsException(String msg, Throwable e){
		super(msg, e);
	}

}
