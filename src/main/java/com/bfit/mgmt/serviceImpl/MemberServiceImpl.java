package com.bfit.mgmt.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bfit.mgmt.config.S3ServiceConfig;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private S3ServiceConfig s3ServiceConfig;

	@Autowired
	private MemberRepo memberRepo;

	@Override
	public Member getMemberById(UUID id) {
		try {
			var response = memberRepo.findById(id);
			if (ObjectUtils.isEmpty(response)) {
				return (Member) response.orElse(null);
			}
			return (Member) response.get();
		} catch (Exception e) {
			throw new RuntimeException();

		}
	}

	@Override
	public Member saveMember(Member member) {
		try {
			UUID id = UUID.randomUUID();
			String profileUrl = s3ServiceConfig.uploadFile(member.getProfileImg(), id.toString());
			var joiningDate = LocalDate.now();
			Member memberReqBdy = new Member(id, profileUrl, member.getMemberName(), member.getEmail(),
					member.getPhoneNumber(), member.getStatus(), joiningDate);
			return memberRepo.save(memberReqBdy);
		} catch (Exception exception) {
			throw new RuntimeException();
		}
	}

	@Override
	public Member updateMember(UUID id, Member updatedMemeber) {
		try {
			String profileUrl = s3ServiceConfig.uploadFile(updatedMemeber.getProfileImg(), id.toString());
			var memberRes = memberRepo.findById(id);
			if (memberRes.isPresent()) {
				memberRes.map(memberObj -> {
					memberObj.setProfileUrl(profileUrl);
					memberObj.setMemberName(updatedMemeber.getMemberName());
					memberObj.setEmail(updatedMemeber.getEmail());
					memberObj.setPhoneNumber(updatedMemeber.getPhoneNumber());
					memberObj.setStatus(updatedMemeber.getStatus());
					return memberRepo.save(memberObj);
				}).orElseThrow(() -> new RuntimeException("Member not found with id : {}" + id));
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	public String dltMemberById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMisuseException("Request ID not found");
			}
			memberRepo.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return "Data Deleted Successfully";
	}

	@Override
	public List<Member> getMemberList() {
		try {
			return memberRepo.findAll();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
