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
import com.bfit.mgmt.entity.Coach;
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
			UUID id = UUID.randomUUID();
			if (!Objects.isNull(profileImg)) {
				profileUrl = s3ServiceConfig.uploadFile(profileImg, id);
			}
			var status = true;
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			var coachReqBdy = new Coach(id, profileUrl, coachRequest.getCoachName(), coachRequest.getEmail(),
					coachRequest.getPhoneNumber(), status, joiningDate, createdAt, createdAt);
			coachRepo.save(coachReqBdy);
			return new ApiResponse(HttpStatus.OK, "Coach details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist coach: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while saving data", true);
	}

	@Override
	public ApiResponse getCoachById(UUID id) {
		Optional<Coach> response = null;
		try {
			response = coachRepo.findById(id);
			if (!ObjectUtils.isEmpty(response)) {
				return new ApiResponse(HttpStatus.OK, response, false);
			}
			log.error("Not found error getting member by ID: {}", id);
		} catch (Exception e) {
			log.error("Error fetching coach by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, response, false);
	}

	@Override
	public ApiResponse updateCoach(UUID id, MultipartFile profileImg, CoachRequest updatedCoach) {
		Optional<Coach> response = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}

			var name = updatedCoach.getCoachName();
			var email = updatedCoach.getEmail();
			var phNumber = updatedCoach.getPhoneNumber();
			var status = updatedCoach.getStatus();

			var existingCoach = coachRepo.findById(id);
			if (existingCoach.isPresent()) {
				var extCoachObj = existingCoach.get();
				if (profileImg != null && !profileImg.isEmpty()) {
					if (extCoachObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extCoachObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg, id);
					extCoachObj.setProfileUrl(newProfileUrl);
				}
				if (ObjectUtils.isNotEmpty(name)) {
					extCoachObj.setCoachName(updatedCoach.getCoachName());
				}
				if (ObjectUtils.isNotEmpty(email)) {
					extCoachObj.setEmail(updatedCoach.getEmail());
				}
				if (ObjectUtils.isNotEmpty(phNumber)) {
					extCoachObj.setPhoneNumber(updatedCoach.getPhoneNumber());
				}
				if (ObjectUtils.isNotEmpty(status)) {
					extCoachObj.setStatus(updatedCoach.getStatus());
				}
				extCoachObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				coachRepo.save(extCoachObj);
				response = coachRepo.findById(id);
				return new ApiResponse(HttpStatus.OK, "Coach details updated successfully", response, false);
			} else {
				log.error("Not found error getting coach by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update coach by ID:{} ", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while updating data", true);
	}

	@Override
	public ApiResponse dltCoachById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var coachPresentRes = coachRepo.findById(id);
			if (!ObjectUtils.isEmpty(coachPresentRes)) {
				if (coachPresentRes.get().getProfileUrl() != null) {
					s3ServiceConfig.deleteFile(coachPresentRes.get().getProfileUrl());
				}
				coachRepo.deleteById(id);
				return new ApiResponse(HttpStatus.OK, "Coach deleted successfully", false);
			}
			log.error("Not found error getting coach by ID: {}", id);
		} catch (Exception e) {
			log.error("Error getting while deleting coach by ID {}", id);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while deleting data", true);
	}

	@Override
	public ApiResponse getCoachList() {
		List<Coach> coachListRes = null;
		try {
			coachListRes = coachRepo.findByStatusTrue();
			return new ApiResponse(HttpStatus.OK, coachListRes, false);
		} catch (Exception e) {
			log.error("Error while getting all the coach");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting list of data", false);
	}

	@Override
	public ApiResponse getCoachCount() {
		Integer countResult = null;
		try {
			countResult = (int) coachRepo.count();
			return new ApiResponse(HttpStatus.OK, countResult, false);
		} catch (Exception e) {
			log.error("Error while getting coach count");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting coach count", true);
	}

}
