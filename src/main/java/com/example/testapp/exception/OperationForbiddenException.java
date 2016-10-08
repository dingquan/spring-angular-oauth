package com.example.testapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN) 
public class OperationForbiddenException extends RuntimeException {
	
	private static final long serialVersionUID = -7041700644201342706L;

	public OperationForbiddenException(String msg) {
		super(msg);
	}
	
	public OperationForbiddenException(String msg, Throwable e) {
		super(msg, e);
	}
}
