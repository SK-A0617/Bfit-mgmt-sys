package com.bfit.mgmt.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface MemberService {

	/*
	 * Get Member By Id
	 * @Param Id
	 */
	ApiResponse getMemberById(UUID id);

	/*
	 * Create Member
	 * @Param profileImg
	 * @Body memberRequest
	 */
	ApiResponse createMember(MultipartFile profileImg, MemberRequest memberRequest);
	
	/*
	 * Update Member
	 * @Param id
	 * @Param profileImg
	 * @Body memberRequest
	 */
	ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest memberRequest);

	/*
	 * Delete Member By Id
	 * @Param id
	 */
	ApiResponse dltMemberById(UUID id);

	/*
	 * Get Member List
	 */
	ApiResponse getMemberList();

	/*
	 * Get Member Count
	 */
	ApiResponse getMemberCount();
	
	/*
	 * Get Member Billing By Id
	 * @Param id
	 */
	ApiResponse getMemberBillingById(UUID id);

}
