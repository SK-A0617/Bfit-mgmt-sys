package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.procedure.ParameterMisuseException;
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
		} catch (Exception exception) {
			throw new RuntimeException();
		}
	}

	@Override
	public Admin getDataById(UUID id) {
		try {
			var response = adminRepo.findById(id);
			if(ObjectUtils.isEmpty(response)) {
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
				adminRes.map(adminObj -> {
					adminObj.setFirstName(updatedAdmin.getFirstName());
					adminObj.setLastName(updatedAdmin.getLastName());
					adminObj.setEmail(updatedAdmin.getEmail());
					adminObj.setPassword(updatedAdmin.getPassword());
					adminObj.setPhoneNumber(updatedAdmin.getPhoneNumber());
					adminObj.setRole(updatedAdmin.getRole());
					return adminRepo.save(adminObj);
				}).orElseThrow(() -> new RuntimeException("Admin not found with id : {}" + id));
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	public String dltAdminById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMisuseException("Request ID not found");
			}
			adminRepo.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return "Data Deleted Successfully";
	}

	@Override
	public List<Admin> getAdminList() {
		try {
			return adminRepo.findAll();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
