package com.bfit.mgmt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterMissingException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = -239957973119953436L;

	public ParameterMissingException(String message) {
		super(message);
	}

}
