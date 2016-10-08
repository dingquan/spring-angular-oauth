package com.example.testapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UserNotLoggedInException extends RuntimeException {

	private static final long serialVersionUID = -6425507490347200696L;
	
	public UserNotLoggedInException(String msg){
		super(msg);
	}
		
	public UserNotLoggedInException(String msg, Throwable e) {
		super(msg, e);
	}
}
