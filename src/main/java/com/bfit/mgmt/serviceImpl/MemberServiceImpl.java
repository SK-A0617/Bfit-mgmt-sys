package com.bfit.mgmt.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.service.MemberService;

public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberRepo memberRepo;

	@Override
	public Member getMemberById(UUID id) {
		try {
			var response = memberRepo.findById(id);
			if(ObjectUtils.isEmpty(response)) {
				return (Member) response.orElse(null);
			}
			return (Member) response.get();
		} catch (Exception e) {
			throw new RuntimeException();
			
		}
	}

	@Override
	public Member saveMember(Member memeber) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Member updateMember(UUID id, Member memeber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dltMemberById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Member> getMemberList() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
