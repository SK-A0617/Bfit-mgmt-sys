package com.bfit.mgmt.service;

import java.util.UUID;

import com.bfit.mgmt.request.AdminRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface AdminService {

	/**
	 * Get Admin By ID
	 * @param id
	 * @return
	 */
	ApiResponse getAdminById(UUID id);

	/**
	 * Save Admin
	 * @param adminRequest
	 * @return
	 */
	ApiResponse createAdmin(AdminRequest adminRequest);
	
	/**
	 * Update Admin
	 * @param id
	 * @param adminRequest
	 * @return
	 */
	ApiResponse updateAdmin(UUID id, AdminRequest adminRequest);
	
	/**
	 * Delete Admin By ID
	 * @param id
	 * @return
	 */
	ApiResponse dltAdminById(UUID id);
	
	/**
	 * Get Admin List
	 * @return
	 */
	ApiResponse getAdminList();
	
}
