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
import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.config.S3ServiceConfig;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.exceptions.DataNotFoundException;
import com.bfit.mgmt.exceptions.ParameterMissingException;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.service.MemberService;
import com.bfit.mgmt.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	private S3ServiceConfig s3ServiceConfig;

	@Autowired
	private MemberRepo memberRepo;

	@Override
	public ApiResponse createMember(MultipartFile profileImg, MemberRequest memberRequest) {
		try {
			String profileUrl = null;
			if (ObjectUtils.isEmpty(memberRequest.getMemberName()) || ObjectUtils.isEmpty(memberRequest.getEmail())
					|| ObjectUtils.isEmpty(memberRequest.getPhoneNumber())) {
				throw new ParameterMissingException("All input parameters are required");
			}
			if (ObjectUtils.isNotEmpty(profileImg)) {
				System.out.println("INside the profile is present condition");
				profileUrl = s3ServiceConfig.uploadFile(profileImg);
			}
			var status = true;
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			var memberReqBdy = new Member(profileUrl, memberRequest.getMemberName(), memberRequest.getEmail(),
					memberRequest.getPhoneNumber(), status, joiningDate, createdAt, createdAt);
			memberRepo.save(memberReqBdy);
			return new ApiResponse(HttpStatus.OK, "Member details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist member: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Error while saving data", true);
	}

	@Override
	public ApiResponse getMemberById(UUID id) {
		Optional<Member> response = null;
		try {
			response = memberRepo.findById(id);
			if (response.isEmpty()) {
				log.error("Not found error getting member by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Error fetching member by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, response, false);
	}

	@Override
	public ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest updatedMemeber) {
		Optional<Member> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			if (ObjectUtils.isEmpty(updatedMemeber)) {
				throw new DataNotFoundException("Request should not be empty");
			}
			var existingMember = memberRepo.findById(id);
			if (existingMember.isPresent()) {
				var extMemberObj = existingMember.get();
				// if (profileImg != null && !profileImg.isEmpty()) {
				if (ObjectUtils.isNotEmpty(profileImg)) {
					if (extMemberObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extMemberObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg);
					extMemberObj.setProfileUrl(newProfileUrl);
				}
				if (ObjectUtils.isNotEmpty(updatedMemeber.getMemberName())) {
					extMemberObj.setMemberName(updatedMemeber.getMemberName());
				}
				if (ObjectUtils.isNotEmpty(updatedMemeber.getEmail())) {
					extMemberObj.setEmail(updatedMemeber.getEmail());
				}
				if (ObjectUtils.isNotEmpty(updatedMemeber.getPhoneNumber())) {
					extMemberObj.setPhoneNumber(updatedMemeber.getPhoneNumber());
				}
				if (ObjectUtils.isNotEmpty(updatedMemeber.getStatus())) {
					extMemberObj.setStatus(updatedMemeber.getStatus());
				}
				extMemberObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				memberRepo.save(extMemberObj);
				response = memberRepo.findById(id);
			} else {
				log.error("Not found error getting member by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update member by ID:{} ", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Member details updated successfully", response, false);
	}

	@Override
	public ApiResponse dltMemberById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var memberPresentRes = memberRepo.findById(id);
			if (memberPresentRes.isPresent()) {
				if (memberPresentRes.get().getProfileUrl() != null) {
					s3ServiceConfig.deleteFile(memberPresentRes.get().getProfileUrl());
				}
				memberRepo.deleteById(id);
			}
			log.error("Not found error getting member by ID: {}", id);
			return new ApiResponse(HttpStatus.NOT_FOUND, "Data Not Found", true);
		} catch (Exception e) {
			log.error("Error getting while deleting member by ID {}", id);
		}
		return new ApiResponse(HttpStatus.OK, "Member deleted successfully", false);
	}

	@Override
	public ApiResponse getMemberList() {
		List<Member> memberListRes = null;
		try {
			memberListRes = memberRepo.findAll();
			if (ObjectUtils.isEmpty(memberListRes)) {
				log.error("Not found error while getting member list");
			}
		} catch (Exception e) {
			log.error("Error while getting all the members");
		}
		return new ApiResponse(HttpStatus.OK, memberListRes, false);
	}

}
