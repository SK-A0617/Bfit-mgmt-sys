package com.bfit.mgmt.service;

import java.util.List;
import java.util.UUID;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.util.ApiResponse;

public interface AdminService {

	ApiResponse getAdminById(UUID id);

	ApiResponse saveAdmin(Admin adminRequest);
	
	ApiResponse updateAdmin(UUID id, Admin admin);
	
	ApiResponse dltAdminById(UUID id);
	
	List<Admin> getAdminList();
	
}
