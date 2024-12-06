package com.bfit.mgmt.service;

import java.util.Optional;
import java.util.UUID;

import com.bfit.mgmt.entity.Admin;

public interface AdminService {

	public Optional<Admin> getDataById(UUID id);

	public Admin saveAdmin(Admin admin);
	
}
