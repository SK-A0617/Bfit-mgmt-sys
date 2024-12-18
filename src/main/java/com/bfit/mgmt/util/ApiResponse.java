package com.bfit.mgmt.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private String message;
	private int statusCode;
	private T data;
	private LocalDateTime timestamp;

	// constructors

	public ApiResponse(HttpStatus httpStatus, T data) {
		this.statusCode = httpStatus.value();
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}

	public ApiResponse(HttpStatus httpStatus, String message) {
		this.statusCode = httpStatus.value();
		this.data = null;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	// Getters and Setters

//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public HttpStatus getHttpStatus() {
//		return httpStatus;
//	}
//
//	public void setHttpStatus(HttpStatus httpStatus) {
//		this.httpStatus = httpStatus;
//	}
//
//	public T getData() {
//		return data;
//	}
//
//	public void setData(T data) {
//		this.data = data;
//	}
//
//	public LocalDateTime getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(LocalDateTime timestamp) {
//		this.timestamp = timestamp;
//	}

}
