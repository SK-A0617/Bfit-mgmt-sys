package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.exceptions.DataNotFoundException;
import com.bfit.mgmt.repo.AdminRepo;
import com.bfit.mgmt.service.AdminService;
import com.bfit.mgmt.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepo adminRepo;

	@Override
	public Admin saveAdmin(Admin admin) {
		try {
			var startingDate = LocalDate.now();
			Admin adminReqBdy = new Admin(admin.getName(), admin.getEmail(), admin.getPassword(),
					admin.getPhoneNumber(), startingDate);
			return adminRepo.save(adminReqBdy);
		} catch (Exception e) {
			log.error("Failed error while persist admin: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to save admin: " + e.getMessage(), e);
		}
	}

	@Override
	public Admin getAdminById(UUID id) {
		try {
			var response = adminRepo.findById(id);
			return response.get();
		} catch (Exception e) {
			log.error("Error fetching admin by ID: {}",id, e.getMessage(), e);
		}
		throw new RuntimeException();
	}

	@Override
	public Admin updateAdmin(UUID id, Admin updatedAdmin) {
		try {
			var adminRes = adminRepo.findById(id);
			if (adminRes.isPresent()) {
				var extAdminObj = adminRes.get();
				extAdminObj.setName(updatedAdmin.getName());
				extAdminObj.setEmail(updatedAdmin.getEmail());
				extAdminObj.setPassword(updatedAdmin.getPassword());
				extAdminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
				return adminRepo.save(extAdminObj);
			} else {
				log.error("Not found error getting admin by ID: {}", id);
				throw new RuntimeException("Admin not found with ID : {}" + id);
			}
		} catch (Exception e) {
			log.error("Failed to update admin by ID: " + e.getMessage(), e);
			throw new RuntimeException("Failed to update admin: " + e.getMessage(), e);
		}
	}

	@Override
	public String dltAdminById(UUID id) {
		try {
			var adminPresentRes = adminRepo.findById(id);
			if (adminPresentRes.isPresent()) {
				adminRepo.deleteById(id);
			} else {
				log.error("Not found error getting admin by ID: {}", id);
				throw new RuntimeException("Admin not found with ID : {}" + id);
			}
		} catch (Exception e) {
			log.error("Error getting while deleting admin by ID {}", id);
			throw new RuntimeException("Failed to delete admin: " + e.getMessage(), e);
		}
		return "Data Deleted Successfully";
	}

	@Override
	public List<Admin> getAdminList() {
		try {
			var adminListRes = adminRepo.findAll();
			if (adminListRes.isEmpty()) {
				log.error("Not found error while getting admin list");
				throw new RuntimeException("No more admin records");
			}
			return adminListRes;
		} catch (Exception e) {
			log.error("Error while getting all the admin");
			throw new RuntimeException("Failed to get all admin" + e.getMessage(), e);
		}
	}

}
