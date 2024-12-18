package com.bfit.mgmt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.entity.Member;

public interface MemberService {

	Member getMemberById(UUID id);

	Member saveMember(MultipartFile profileImg, Member memeber);

	Member updateMember(UUID id, MultipartFile profileImg, Member memeber);

	void dltMemberById(UUID id);

	List<Member> getMemberList();

}
