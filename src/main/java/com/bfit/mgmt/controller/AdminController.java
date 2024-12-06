package com.bfit.mgmt.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.service.AdminService;

@RestController
@RequestMapping(path = "/app/v1")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/saveAdmin")
	public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin){
		var data = adminService.saveAdmin(admin);
		return new ResponseEntity<Admin>(data, HttpStatus.CREATED);
	}
	
	@GetMapping("/getById")
	public ResponseEntity<Admin> getAdminDataById(@RequestParam UUID id){
		Optional<Admin> adminResponse = adminService.getDataById(id);
		if(adminResponse.isPresent()) {
			return new ResponseEntity<Admin>(adminResponse.get(), HttpStatus.OK);
		}else {
			return new ResponseEntity<Admin>(HttpStatus.NOT_FOUND);
		}
	}
	
}
