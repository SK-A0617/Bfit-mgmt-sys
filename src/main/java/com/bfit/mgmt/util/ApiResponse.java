package com.bfit.mgmt.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private String message;
	private int status;
	private T data;
	private LocalDateTime timestamp;

	// constructors

	public ApiResponse(HttpStatus httpStatus, T data) {
		this.status = httpStatus.value();
		this.data = data;
		this.message = "";
		this.timestamp = LocalDateTime.now();
	}

	public ApiResponse(HttpStatus httpStatus, String message) {
		this.status = httpStatus.value();
		this.data = null;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

}
