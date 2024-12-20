package com.bfit.mgmt.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.service.AdminService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/saveAdmin")
	public ApiResponse<Admin> saveAdmin(@RequestBody Admin admin) {
		var data = adminService.saveAdmin(admin);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@GetMapping("/getAdminById")
	public ApiResponse<Admin> getAdminDataById(@RequestParam UUID id) {
		var data = adminService.getDataById(id);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@GetMapping("/getAllAdminList")
	public ApiResponse<List<Admin>> getAdminList() {
		var data = adminService.getAdminList();
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@PutMapping("/updateAdmin/{id}")
	public ApiResponse<Admin> updateAdmin(@PathVariable UUID id, @RequestBody Admin admin) {
		var data = adminService.updateAdmin(id, admin);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@DeleteMapping("/deleteAdmin/{id}")
	public ApiResponse<Void> dltAdminById(@PathVariable UUID id) {
		adminService.dltAdminById(id);
		return new ApiResponse<>(HttpStatus.OK, "Admin deleted successfully");
	}

}
