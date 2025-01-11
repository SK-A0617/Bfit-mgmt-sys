package com.bfit.mgmt.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.CoachRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface CoachService {
	
	ApiResponse getCoachById(UUID id);

	ApiResponse createCoach(MultipartFile profileImg, CoachRequest coachRequest);

	ApiResponse updateCoach(UUID id, MultipartFile profileImg, CoachRequest coachRequest);

	ApiResponse dltCoachById(UUID id);

	ApiResponse getCoachList();
	
	ApiResponse getCoachCount();

}
