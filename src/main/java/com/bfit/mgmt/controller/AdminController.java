package com.bfit.mgmt.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.request.AdminRequest;
import com.bfit.mgmt.service.AdminService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;
	
@RestController
@RequestMapping(path = Constants.REQPATH)
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/createAdmin")
	public ResponseEntity<ApiResponse> createAdmin(@RequestBody AdminRequest adminRequest) {
		var data = adminService.createAdmin(adminRequest);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getAdminById")
	public ResponseEntity<ApiResponse> getAdminById(@RequestParam UUID id) {
		var data = adminService.getAdminById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getAllAdminList")
	public ResponseEntity<ApiResponse> getAdminList() {
		var data = adminService.getAdminList();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PutMapping("/updateAdmin/{id}")
	public ResponseEntity<ApiResponse> updateAdmin(@PathVariable UUID id, @RequestBody AdminRequest adminRequest) {
		var data = adminService.updateAdmin(id, adminRequest);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAdmin/{id}")
	public ResponseEntity<ApiResponse> dltAdminById(@PathVariable UUID id) {
		var data = adminService.dltAdminById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}
