package com.bfit.mgmt.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

	private String message;
	private HttpStatus httpStatus;
	private T data;
	private LocalDateTime timestamp;

	// constructors

	public ApiResponse(HttpStatus httpStatus, T data) {
		this.httpStatus = httpStatus;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}

	// Getters and Setters

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
