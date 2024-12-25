package com.bfit.mgmt.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.exceptions.DataNotFoundException;
import com.bfit.mgmt.exceptions.ParameterMissingException;
import com.bfit.mgmt.repo.AdminRepo;
import com.bfit.mgmt.request.AdminRequest;
import com.bfit.mgmt.service.AdminService;
import com.bfit.mgmt.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepo adminRepo;

	@Override
	public ApiResponse createAdmin(AdminRequest adminRequest) {
		try {
			if (ObjectUtils.isEmpty(adminRequest.getName()) || ObjectUtils.isEmpty(adminRequest.getEmail())
					|| ObjectUtils.isEmpty(adminRequest.getPassword())
					|| ObjectUtils.isEmpty(adminRequest.getPhoneNumber())) {
				throw new ParameterMissingException("All input parameters are required");
			}
			var startingDate = LocalDate.now();
			var adminReqBdy = new Admin(adminRequest.getName(), adminRequest.getEmail(), adminRequest.getPassword(),
					adminRequest.getPhoneNumber(), adminRequest.getRole(), startingDate);
			adminRepo.save(adminReqBdy);
			return new ApiResponse(HttpStatus.OK, "Admin details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist admin: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Error while saving data", true);
	}

	@Override
	public ApiResponse getAdminById(UUID id) {
		Optional<Admin> response = null;
		try {
			response = adminRepo.findById(id);
			if (response.isEmpty()) {
				log.error("Not found error getting member by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Error fetching admin by ID: {}", id, e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, response, false);
	}

	@Override
	public ApiResponse updateAdmin(UUID id, AdminRequest updatedAdmin) {
		Optional<Admin> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			if(ObjectUtils.isEmpty(updatedAdmin)) {
				throw new DataNotFoundException("Request should not be empty");
			}
			var existingAdmin = adminRepo.findById(id);
			if (existingAdmin.isPresent()) {
				var extAdminObj = existingAdmin.get();
				if (ObjectUtils.isNotEmpty(updatedAdmin.getName())) {
					extAdminObj.setName(updatedAdmin.getName());
				}
				if (ObjectUtils.isNotEmpty(updatedAdmin.getEmail())) {
					extAdminObj.setEmail(updatedAdmin.getEmail());
				}
				if (ObjectUtils.isNotEmpty(updatedAdmin.getPassword())) {
					extAdminObj.setPassword(updatedAdmin.getPassword());
				}
				if (ObjectUtils.isNotEmpty(updatedAdmin.getPhoneNumber())) {
					extAdminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
				}
				extAdminObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				adminRepo.save(extAdminObj);
				response = adminRepo.findById(id);
			} else {
				log.error("Not found error getting admin by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update admin by ID: " + e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Admin details updated successfully", response, false);
	}

	@Override
	public ApiResponse dltAdminById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var adminPresentRes = adminRepo.findById(id);
			if (ObjectUtils.isEmpty(adminPresentRes)) {
				log.error("Not found error getting admin by ID: {}", id);
				return new ApiResponse(HttpStatus.NOT_FOUND, "Data Not Found", true);
			}
			adminRepo.deleteById(id);
		} catch (Exception e) {
			log.error("Error getting while deleting admin by ID {}", id);
		}
		return new ApiResponse(HttpStatus.OK, "Admin deleted successfully", false);
	}

	@Override
	public ApiResponse getAdminList() {
		List<Admin> adminListRes = null;
		try {
			adminListRes = adminRepo.findAll();
			if (ObjectUtils.isEmpty(adminListRes)) {
				log.error("Not found error while getting admin list");
			}
		} catch (Exception e) {
			log.error("Error while getting all the admin");
		}
		return new ApiResponse(HttpStatus.OK, adminListRes, false);
	}

}
