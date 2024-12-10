package com.bfit.mgmt.service;

import java.util.List;
import java.util.UUID;

import com.bfit.mgmt.entity.Member;

public interface MemberService {
	
	Member getMemberById(UUID id);

	Member saveMember(Member memeber);
	
	Member updateMember(UUID id, Member memeber);
	
	String dltMemberById(UUID id);
	
	List<Member> getMemberList();

}
