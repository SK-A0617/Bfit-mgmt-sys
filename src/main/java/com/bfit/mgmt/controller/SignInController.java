package com.bfit.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.service.AdminService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class SignInController {
	
	@Autowired
	private AdminService adminService;
		
	@PostMapping("/sign-in")
	public ResponseEntity<ApiResponse> signIn(@RequestParam String email, @RequestParam String password){
		var data = adminService.signIn(email, password);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}	
	
}
