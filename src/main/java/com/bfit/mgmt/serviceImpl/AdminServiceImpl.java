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
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while saving data", true);
	}

	@Override
	public ApiResponse getAdminById(UUID id) {
		Optional<Admin> response = null;
		try {
			response = adminRepo.findById(id);
			if (!ObjectUtils.isEmpty(response)) {
				return new ApiResponse(HttpStatus.OK, response, false);
			}
			log.error("Not found error getting admin by ID: {}", id);
			return new ApiResponse(HttpStatus.OK, String.format("Not found error getting admin by ID: %s", id), false);
		} catch (Exception e) {
			log.error("Error fetching admin by ID: {}", id, e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting data", true);
	}

	@Override
	public ApiResponse updateAdmin(UUID id, AdminRequest updatedAdmin) {
		Optional<Admin> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var existingAdmin = adminRepo.findById(id);
			if (existingAdmin.isPresent()) {
				var extAdminObj = existingAdmin.get();

				var name = updatedAdmin.getName();
				var email = updatedAdmin.getEmail();
				var password = updatedAdmin.getPassword();
				var phNumber = updatedAdmin.getPhoneNumber();

				if (ObjectUtils.isNotEmpty(name)) {
					extAdminObj.setName(updatedAdmin.getName());
				}
				if (ObjectUtils.isNotEmpty(email)) {
					extAdminObj.setEmail(updatedAdmin.getEmail());
				}
				if (ObjectUtils.isNotEmpty(password)) {
					extAdminObj.setPassword(updatedAdmin.getPassword());
				}
				if (ObjectUtils.isNotEmpty(phNumber)) {
					extAdminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
				}
				extAdminObj.setRole(updatedAdmin.getRole());
				extAdminObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				adminRepo.save(extAdminObj);
				response = adminRepo.findById(id);
				return new ApiResponse(HttpStatus.OK, "Admin details updated successfully", response, false);
			} else {
				log.error("Not found error getting admin by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update admin by ID: " + e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while updating data", true);
	}

	@Override
	public ApiResponse dltAdminById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var adminPresentRes = adminRepo.findById(id);
			if (!ObjectUtils.isEmpty(adminPresentRes)) {
				adminRepo.deleteById(id);
				return new ApiResponse(HttpStatus.OK, "Admin deleted successfully", false);
			}
			log.error("Not found error getting admin by ID: {}", id);
		} catch (Exception e) {
			log.error("Error getting while deleting admin by ID {}", id);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while deleting data", true);
	}

	@Override
	public ApiResponse getAdminList() {
		List<Admin> adminListRes = null;
		try {
			adminListRes = adminRepo.findAll();
			return new ApiResponse(HttpStatus.OK, adminListRes, false);
		} catch (Exception e) {
			log.error("Error while getting all the admin");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting list of data", true);
	}

}
