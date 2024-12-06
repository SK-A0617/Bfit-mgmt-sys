package com.bfit.mgmt.serviceImpl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.repo.AdminRepo;
import com.bfit.mgmt.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminRepo adminRepo;

	@Override
	public Optional<Admin> getDataById(UUID id) {
		return adminRepo.findById(id);
	}

}
