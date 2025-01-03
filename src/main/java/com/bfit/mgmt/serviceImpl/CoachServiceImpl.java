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
import com.bfit.mgmt.entity.Coach;
import com.bfit.mgmt.exceptions.DataNotFoundException;
import com.bfit.mgmt.exceptions.ParameterMissingException;
import com.bfit.mgmt.repo.CoachRepo;
import com.bfit.mgmt.request.CoachRequest;
import com.bfit.mgmt.service.CoachService;
import com.bfit.mgmt.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoachServiceImpl implements CoachService {

	@Autowired
	private S3ServiceConfig s3ServiceConfig;

	@Autowired
	private CoachRepo coachRepo;

	@Override
	public ApiResponse createCoach(MultipartFile profileImg, CoachRequest coachRequest) {
		try {
			String profileUrl = null;
			if (ObjectUtils.isEmpty(coachRequest.getCoachName()) || ObjectUtils.isEmpty(coachRequest.getEmail())
					|| ObjectUtils.isEmpty(coachRequest.getPhoneNumber())) {
				throw new ParameterMissingException("All input parameters are required");
			}
			if (ObjectUtils.isNotEmpty(profileImg)) {
				profileUrl = s3ServiceConfig.uploadFile(profileImg);
			}
			var status = true;
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			var coachReqBdy = new Coach(profileUrl, coachRequest.getCoachName(), coachRequest.getEmail(),
					coachRequest.getPhoneNumber(), status, joiningDate, createdAt, createdAt);
			coachRepo.save(coachReqBdy);
			return new ApiResponse(HttpStatus.OK, "Coach details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist coach: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Error while saving data", true);
	}

	@Override
	public ApiResponse getCoachById(UUID id) {
		Optional<Coach> response = null;
		try {
			response = coachRepo.findById(id);
			if (response.isEmpty()) {
				log.error("Not found error getting coach by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Error fetching coach by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, response, false);
	}

	@Override
	public ApiResponse updateCoach(UUID id, MultipartFile profileImg, CoachRequest updatedCoach) {
		Optional<Coach> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			if (ObjectUtils.isEmpty(updatedCoach)) {
				throw new DataNotFoundException("Request should not be empty");
			}
			var existingCoach = coachRepo.findById(id);
			if (existingCoach.isPresent()) {
				var extCoachObj = existingCoach.get();
				// if (profileImg != null && !profileImg.isEmpty()) {
				if (ObjectUtils.isNotEmpty(profileImg)) {
					if (extCoachObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extCoachObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg);
					extCoachObj.setProfileUrl(newProfileUrl);
				}
				if (ObjectUtils.isNotEmpty(updatedCoach.getCoachName())) {
					extCoachObj.setCoachName(updatedCoach.getCoachName());
				}
				if (ObjectUtils.isNotEmpty(updatedCoach.getEmail())) {
					extCoachObj.setEmail(updatedCoach.getEmail());
				}
				if (ObjectUtils.isNotEmpty(updatedCoach.getPhoneNumber())) {
					extCoachObj.setPhoneNumber(updatedCoach.getPhoneNumber());
				}
				if (ObjectUtils.isNotEmpty(updatedCoach.getStatus())) {
					extCoachObj.setStatus(updatedCoach.getStatus());
				}
				extCoachObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				coachRepo.save(extCoachObj);
				response = coachRepo.findById(id);
			} else {
				log.error("Not found error getting coach by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update coach by ID:{} ", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.OK, "Coach details updated successfully", response, false);
	}

	@Override
	public ApiResponse dltCoachById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var coachPresentRes = coachRepo.findById(id);
			if (coachPresentRes.isPresent()) {
				if (coachPresentRes.get().getProfileUrl() != null) {
					s3ServiceConfig.deleteFile(coachPresentRes.get().getProfileUrl());
				}
				coachRepo.deleteById(id);
			}
			log.error("Not found error getting coach by ID: {}", id);
			return new ApiResponse(HttpStatus.NOT_FOUND, "Data Not Found", true);
		} catch (Exception e) {
			log.error("Error getting while deleting coach by ID {}", id);
		}
		return new ApiResponse(HttpStatus.OK, "Coach deleted successfully", false);
	}

	@Override
	public ApiResponse getCoachList() {
		List<Coach> coachListRes = null;
		try {
			coachListRes = coachRepo.findAll();
			if (ObjectUtils.isEmpty(coachListRes)) {
				log.error("Not found error while getting coach list");
			}
		} catch (Exception e) {
			log.error("Error while getting all the coach");
		}
		return new ApiResponse(HttpStatus.OK, coachListRes, false);
	}

}
