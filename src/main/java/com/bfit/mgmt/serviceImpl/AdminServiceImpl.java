package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.exceptions.ParameterMissingException;
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
	public ApiResponse saveAdmin(Admin adminRequest) {
		try {
			var startingDate = LocalDate.now();
			var adminReqBdy = new Admin(adminRequest.getName(), adminRequest.getEmail(), adminRequest.getPassword(),
					adminRequest.getPhoneNumber(), startingDate);
			adminRepo.save(adminReqBdy);
		} catch (Exception e) {
			log.error("Failed error while persist admin: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Admin details saved successfully", false);
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
	public ApiResponse updateAdmin(UUID id, Admin updatedAdmin) {
		Optional<Admin> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMisuseException("Id is missing");
			}
			var adminRes = adminRepo.findById(id);
			if (adminRes.isPresent()) {
				var extAdminObj = adminRes.get();
				extAdminObj.setName(updatedAdmin.getName());
				extAdminObj.setEmail(updatedAdmin.getEmail());
				extAdminObj.setPassword(updatedAdmin.getPassword());
				extAdminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
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
			if (adminPresentRes.isEmpty()) {
				log.error("Not found error getting admin by ID: {}", id);
			}
			adminRepo.deleteById(id);
		} catch (Exception e) {
			log.error("Error getting while deleting admin by ID {}", id);
		}
		return new ApiResponse(HttpStatus.OK, "Admin deleted successfully", false);
	}

	@Override
	public List<Admin> getAdminList() {
		List<Admin> adminListRes = null;
		try {
			adminListRes = adminRepo.findAll();
			if (adminListRes.isEmpty()) {
				log.error("Not found error while getting admin list");
			}
		} catch (Exception e) {
			log.error("Error while getting all the admin");
		}
		return adminListRes;
	}

}
