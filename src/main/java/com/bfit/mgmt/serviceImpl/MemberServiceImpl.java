package com.bfit.mgmt.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.config.S3ServiceConfig;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	private S3ServiceConfig s3ServiceConfig;

	@Autowired
	private MemberRepo memberRepo;

	@Override
	public Member getMemberById(UUID id) {
		try {
			var response = memberRepo.findById(id);
			if (response.isEmpty()) {
				log.error("Not found error getting member by ID: {}", id);
				throw new RuntimeException("Member not found with id :" + id);
			}
			return response.get();
		} catch (Exception e) {
			log.error("Error fetching member by ID: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to get Member: " + e.getMessage(), e);
		}
	}

	@Override
	public Member saveMember(MultipartFile profileImg, Member member) {
		try {
			String profileUrl = s3ServiceConfig.uploadFile(profileImg);
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			Member memberReqBdy = new Member(profileUrl, member.getMemberName(), member.getEmail(),
					member.getPhoneNumber(), member.getStatus(), joiningDate, createdAt, createdAt);
			return memberRepo.save(memberReqBdy);
		} catch (Exception e) {
			log.error("Failed error while persist member: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to save member: " + e.getMessage(), e);
		}
	}

	@Override
	public Member updateMember(UUID id, MultipartFile profileImg, Member updatedMemeber) {
		try {
			var memberRes = memberRepo.findById(id);
			if (memberRes.isPresent()) {
				var extMemberObj = memberRes.get();
				if (profileImg != null && !profileImg.isEmpty()) {
					if (extMemberObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extMemberObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg);
					extMemberObj.setProfileUrl(newProfileUrl);
				}
				extMemberObj.setMemberName(updatedMemeber.getMemberName());
				extMemberObj.setEmail(updatedMemeber.getEmail());
				extMemberObj.setPhoneNumber(updatedMemeber.getPhoneNumber());
				extMemberObj.setStatus(updatedMemeber.getStatus());

				return memberRepo.save(extMemberObj);
			} else {
				log.error("Not found error getting member by ID: {}", id);
				throw new RuntimeException("Member not found with ID :" + id);
			}
		} catch (Exception e) {
			log.error("Failed to update member by ID:{} " , e.getMessage(), e);
			throw new RuntimeException("Failed to update Member: {}" + e.getMessage(), e);
		}
	}

	@Override
	public void dltMemberById(UUID id) {
		try {
			var memberPresentRes = memberRepo.findById(id);
			if (memberPresentRes.isPresent()) {
				if (memberPresentRes.get().getProfileUrl() != null) {
					s3ServiceConfig.deleteFile(memberPresentRes.get().getProfileUrl());
				}
				memberRepo.deleteById(id);
			} else {
				log.error("Not found error getting member by ID: {}", id);
				throw new RuntimeException("Member not found with ID :" + id);
			}
		} catch (Exception e) {
			log.error("Error getting while deleting member by ID {}", id);
			throw new RuntimeException("Failed to delete Member: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Member> getMemberList() {
		try {
			var memberListRes = memberRepo.findAll();
			if(memberListRes.isEmpty()) {
				log.error("Not found error while getting member list");
				throw new RuntimeException("No more member records");
			}
			return memberListRes;
		} catch (Exception e) {
			log.error("Error while getting all the members");
			throw new RuntimeException("Failed to get all members: " + e.getMessage(), e);
		}
	}
	
}
