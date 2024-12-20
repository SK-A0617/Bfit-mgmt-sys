package com.bfit.mgmt.service;

import java.util.List;
import java.util.UUID;

import com.bfit.mgmt.entity.Admin;

public interface AdminService {

	Admin getDataById(UUID id);

	Admin saveAdmin(Admin admin);
	
	Admin updateAdmin(UUID id, Admin admin);
	
	void dltAdminById(UUID id);
	
	List<Admin> getAdminList();
	
}
