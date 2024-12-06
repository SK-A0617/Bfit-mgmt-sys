package com.bfit.mgmt.util;

import java.time.LocalDateTime;

public class ApiResponse<T> {
	
	private String status;
	private String message;
	private T data;
	private LocalDateTime timestamp;
	
	public ApiResponse(String status, T data) {
		this.status = status;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}
	
	public ApiResponse(String status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
