package com.bfit.mgmt.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.CoachRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface CoachService {
	
	/**
	 * Get Coach By ID
	 * @param id
	 * @return
	 */
	ApiResponse getCoachById(UUID id);

	/**
	 * Save Coach
	 * @param profileImg
	 * @param coachRequest
	 * @return
	 */
	ApiResponse createCoach(MultipartFile profileImg, CoachRequest coachRequest);

	/**
	 * Update Coach
	 * @param id
	 * @param profileImg
	 * @param coachRequest
	 * @return
	 */
	ApiResponse updateCoach(UUID id, MultipartFile profileImg, CoachRequest coachRequest);

	/**
	 * Delete Coach By ID
	 * @param id
	 * @return
	 */
	ApiResponse dltCoachById(UUID id);

	/**
	 * Get Coach List
	 * @return
	 */
	ApiResponse getCoachList();
	
	/**
	 * Get Coach Count
	 * @return
	 */
	ApiResponse getCoachCount();

}
