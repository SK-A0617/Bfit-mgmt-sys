package com.bfit.mgmt.util;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse {
	
	private Integer status;
	private String message;
	private Object response;
	private Long timestamp;
	private Boolean errorType;

	// constructors

	public ApiResponse(HttpStatus status, String message, Object response, Boolean errorType) {
		this.status = status.value();
		this.message = message;
		this.response = response;
		this.timestamp = System.currentTimeMillis();
		this.errorType = errorType;
	}
	
	public ApiResponse(HttpStatus status, Object response, Boolean errorType) {
		this.status = status.value();
		this.message = "";
		this.response = response;
		this.timestamp = System.currentTimeMillis();
		this.errorType = errorType;
	}
	
	public ApiResponse(HttpStatus status, String message, Boolean errorType) {
		this.status = status.value();
		this.message = message;
		this.timestamp = System.currentTimeMillis();
		this.errorType = errorType;
	}

}
