package com.bfit.mgmt.service;

import java.util.UUID;

import com.bfit.mgmt.request.AdminRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface AdminService {

	ApiResponse getAdminById(UUID id);

	ApiResponse createAdmin(AdminRequest adminRequest);
	
	ApiResponse updateAdmin(UUID id, AdminRequest adminRequest);
	
	ApiResponse dltAdminById(UUID id);
	
	ApiResponse getAdminList();
	
}
