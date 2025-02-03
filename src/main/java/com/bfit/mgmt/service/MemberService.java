package com.bfit.mgmt.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.util.ApiResponse;

public interface MemberService {

	ApiResponse getMemberById(UUID id);

	ApiResponse createMember(MultipartFile profileImg, MemberRequest memberRequest, String category, String paymentStatus);

	ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest memberRequest);

	ApiResponse dltMemberById(UUID id);

	ApiResponse getMemberList();

	ApiResponse getMemberCount();

}
