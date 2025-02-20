package com.bfit.mgmt.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface MemberService {
	
	/**
	 * Get Member By Id
	 * @param id
	 * @return
	 */
	ApiResponse getMemberById(UUID id);
	
	/**
	 * Create Member
	 * @param profileImg
	 * @param memberRequest
	 * @return
	 */
	ApiResponse createMember(MultipartFile profileImg, MemberRequest memberRequest);
	
	/**
	 * Update Member
	 * @param id
	 * @param profileImg
	 * @param memberRequest
	 * @return
	 */
	ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest memberRequest);
	
	/**
	 * Delete Member By Id
	 * @param id
	 * @return
	 */
	ApiResponse dltMemberById(UUID id);

	/**
	 * Get Member List
	 * @return
	 */
	ApiResponse getMemberList();
	
	/**
	 * Get Member Count
	 * @return
	 */
	ApiResponse getMemberCount();
	
	/**
	 * Get Member Billing By Id
	 * @param id
	 * @return
	 */
	ApiResponse getMemberBillingById(UUID id);

}
