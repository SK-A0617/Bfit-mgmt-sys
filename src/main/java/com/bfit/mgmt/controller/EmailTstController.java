package com.bfit.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.service.EmailRemService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class EmailTstController {
	
	@Autowired
	private EmailRemService emailRemService;
	
	@GetMapping("/send-mail")
	public ResponseEntity<ApiResponse> testMail() {
		var data = emailRemService.sendRemainderMail();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}
