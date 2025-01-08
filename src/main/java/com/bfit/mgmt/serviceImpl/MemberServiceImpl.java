package com.bfit.mgmt.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.config.S3ServiceConfig;
import com.bfit.mgmt.entity.Member;
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
			UUID id = UUID.randomUUID();
			if (!Objects.isNull(profileImg)) {
				profileUrl = s3ServiceConfig.uploadFile(profileImg, id);
			}
			var status = true;
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			var memberReqBdy = new Member(id, profileUrl, memberRequest.getMemberName(), memberRequest.getEmail(),
					memberRequest.getPhoneNumber(), status, joiningDate, createdAt, createdAt);
			memberRepo.save(memberReqBdy);
			return new ApiResponse(HttpStatus.OK, "Member details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist member: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while saving data", true);
	}

	@Override
	public ApiResponse getMemberById(UUID id) {
		Optional<Member> response = null;
		try {
			response = memberRepo.findById(id);
			if (!ObjectUtils.isEmpty(response)) {
				return new ApiResponse(HttpStatus.OK, response, false);
			}
			log.error("Not found error getting member by ID: {}", id);
		} catch (Exception e) {
			log.error("Error fetching member by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting data", true);
	}

	@Override
	public ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest updatedMember) {
		Optional<Member> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var existingMember = memberRepo.findById(id);
			if (existingMember.isPresent()) {

				var name = updatedMember.getMemberName();
				var email = updatedMember.getEmail();
				var phNumber = updatedMember.getPhoneNumber();
				var status = updatedMember.getStatus();

				var extMemberObj = existingMember.get();
				if (profileImg != null && !profileImg.isEmpty()) {
					if (extMemberObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extMemberObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg, id);
					extMemberObj.setProfileUrl(newProfileUrl);
				}
				if (ObjectUtils.isNotEmpty(name)) {
					extMemberObj.setMemberName(updatedMember.getMemberName());
				}
				if (ObjectUtils.isNotEmpty(email)) {
					extMemberObj.setEmail(updatedMember.getEmail());
				}
				if (ObjectUtils.isNotEmpty(phNumber)) {
					extMemberObj.setPhoneNumber(updatedMember.getPhoneNumber());
				}
				if (ObjectUtils.isNotEmpty(status)) {
					extMemberObj.setStatus(updatedMember.getStatus());
				}
				extMemberObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				memberRepo.save(extMemberObj);
				response = memberRepo.findById(id);
				return new ApiResponse(HttpStatus.OK, "Member details updated successfully", response, false);
			} else {
				log.error("Not found error getting member by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update member by ID:{} ", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while updating data", true);
	}

	@Override
	public ApiResponse dltMemberById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var memberPresentRes = memberRepo.findById(id);
			if (!ObjectUtils.isEmpty(memberPresentRes)) {
				if (memberPresentRes.get().getProfileUrl() != null) {
					s3ServiceConfig.deleteFile(memberPresentRes.get().getProfileUrl());
				}
				memberRepo.deleteById(id);
				return new ApiResponse(HttpStatus.OK, "Member deleted successfully", false);
			}
			log.error("Not found error getting member by ID: {}", id);
		} catch (Exception e) {
			log.error("Error getting while deleting member by ID {}", id);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while deleting data", true);
	}

	@Override
	public ApiResponse getMemberList() {
		List<Member> memberListRes = null;
		try {
			memberListRes = memberRepo.findAll();
			return new ApiResponse(HttpStatus.OK, memberListRes, false);
		} catch (Exception e) {
			log.error("Error while getting all the members");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting list of data", true);
	}

}
