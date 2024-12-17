package com.bfit.mgmt.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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
			System.out.println(">>>>>>>"+response.get());
			var profileUrl = response.get().getProfileUrl();
			System.out.println(">>>>>>>"+profileUrl);
			var fileName = profileUrl.substring(profileUrl.lastIndexOf("/")+1);
			System.out.println(">>>>>>>"+fileName);
			var profilePath = s3ServiceConfig.getFile(fileName);
			System.out.println(">>>> getting profile path from s3:"+profilePath);
			if (ObjectUtils.isEmpty(response)) {
				return (Member) response.orElse(null);
			}
			return (Member) response.get();
		} catch (Exception e) {
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
			throw new RuntimeException("Failed to save Member: " + e.getMessage(), e);
		}
	}

	@Override
	public Member updateMember(UUID id, Member updatedMemeber) {
		try {
			String profileUrl = s3ServiceConfig.uploadFile(updatedMemeber.getProfileImg());
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
