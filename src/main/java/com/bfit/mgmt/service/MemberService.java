package com.bfit.mgmt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.util.ApiResponse;

public interface MemberService {

	ApiResponse getMemberById(UUID id);

	ApiResponse saveMember(MultipartFile profileImg, Member memberRequest);

	ApiResponse updateMember(UUID id, MultipartFile profileImg, Member memeber);

	ApiResponse dltMemberById(UUID id);

	List<Member> getMemberList();

}
