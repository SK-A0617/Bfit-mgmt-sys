package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bfit.mgmt.entity.Admin;
import com.bfit.mgmt.repo.AdminRepo;
import com.bfit.mgmt.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepo adminRepo;

	@Override
	public Admin saveAdmin(Admin admin) {
		try {
			var startingDate = LocalDate.now();
			Admin adminReqBdy = new Admin(admin.getFirstName(), admin.getLastName(), admin.getEmail(),
					admin.getPassword(), admin.getPhoneNumber(), admin.getRole(), startingDate);
			return adminRepo.save(adminReqBdy);
		} catch (Exception e) {
			throw new RuntimeException("Failed to admin Member: " + e.getMessage(), e);
		}
	}

	@Override
	public Admin getDataById(UUID id) {
		try {
			var response = adminRepo.findById(id);
			if (ObjectUtils.isEmpty(response)) {
				return response.orElse(null);
			}
			return response.get();
		} catch (Exception e) {
			throw new RuntimeException();

		}
	}

	@Override
	public Admin updateAdmin(UUID id, Admin updatedAdmin) {
		try {
			var adminRes = adminRepo.findById(id);
			if (adminRes.isPresent()) {
				var adminObj = adminRes.get();
				adminObj.setFirstName(updatedAdmin.getFirstName());
				adminObj.setLastName(updatedAdmin.getLastName());
				adminObj.setEmail(updatedAdmin.getEmail());
				adminObj.setPassword(updatedAdmin.getPassword());
				adminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
				adminObj.setRole(updatedAdmin.getRole());

				return adminRepo.save(adminObj);
			} else {
				throw new RuntimeException("Admin not found with id :" + id);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to update admin: " + e.getMessage(), e);
		}
	}

	@Override
	public void dltAdminById(UUID id) {
		try {
			var adminPresentRes = adminRepo.findById(id);
			if (adminPresentRes.isPresent()) {
				adminRepo.deleteById(id);
			} else {
				throw new RuntimeException("Admin not found with id :" + id);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete Admin with id: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Admin> getAdminList() {
		try {
			var response = adminRepo.findAll();
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get admin list: " + e.getMessage(), e);
		}
	}

}
