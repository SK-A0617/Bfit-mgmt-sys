package com.bfit.mgmt.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.config.S3ServiceConfig;
import com.bfit.mgmt.entity.BillingInfo;
import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.exceptions.ParameterMissingException;
import com.bfit.mgmt.repo.BillingInfoRepo;
import com.bfit.mgmt.repo.MemberRepo;
import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.response.BillingResponse;
import com.bfit.mgmt.response.MemberResponse;
import com.bfit.mgmt.service.MemberService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	private S3ServiceConfig s3ServiceConfig;

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private BillingInfoRepo billingInfoRepo;

	@Override
	public ApiResponse createMember(MultipartFile profileImg, MemberRequest memberRequest) {
		try {
			var profileUrl = "";
			if (ObjectUtils.isEmpty(memberRequest.getMemberName()) || ObjectUtils.isEmpty(memberRequest.getEmail())
					|| ObjectUtils.isEmpty(memberRequest.getPhoneNumber())
					|| ObjectUtils.isEmpty(memberRequest.getCategory())
					|| ObjectUtils.isEmpty(memberRequest.getPaidAmount())) {
				throw new ParameterMissingException("All input parameters are required");
			}
			UUID id = UUID.randomUUID();
			if (!Objects.isNull(profileImg)) {
				profileUrl = s3ServiceConfig.uploadFile(profileImg, id);
			}
			var status = true;
			var joiningDate = LocalDate.now();
			var createdAt = new Timestamp(System.currentTimeMillis());
			var memberReqBdy = new Member(id, profileUrl, memberRequest.getMemberName(), memberRequest.getEmail(),
					memberRequest.getPhoneNumber(), status, joiningDate, createdAt, createdAt);
			memberRepo.save(memberReqBdy);
			var billingInfo = saveBillingInfo(id, joiningDate, memberRequest);
			billingInfoRepo.save(billingInfo);
			return new ApiResponse(HttpStatus.OK, "Member details saved successfully", false);
		} catch (Exception e) {
			log.error("Failed error while persist member: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while saving data", true);
	}

	public BillingInfo saveBillingInfo(UUID id, LocalDate joiningDate, MemberRequest memberRequest) {
		UUID billingId = UUID.randomUUID();
		var dueDate = joiningDate.plusMonths(1);
		var categoryPrice = Constants.GENERAL_CAT_AMT;
		var paymentStatus = "";
		var balanceAmt = 0;
		var createdAt = new Timestamp(System.currentTimeMillis());
		if (memberRequest.getCategory().equalsIgnoreCase(Constants.CARDIO_CATEGORY)) {
			categoryPrice = Constants.CARDIO_CAT_AMT;
		} else if (memberRequest.getCategory().equalsIgnoreCase(Constants.GENERAL_AND_CARDIO_CATEGORY)) {
			categoryPrice = Constants.GENERAL_AND_CARDIO_CAT_AMT;
		}
		balanceAmt = categoryPrice - memberRequest.getPaidAmount();
		if (balanceAmt != 0) {
			paymentStatus = Constants.YET_TO_BE_PAID;
		} else {
			paymentStatus = Constants.PAID;
		}
		var billingInfoObj = new BillingInfo(billingId, id, joiningDate, dueDate, memberRequest.getCategory(),
				categoryPrice, memberRequest.getPaidAmount(), balanceAmt, paymentStatus, createdAt, createdAt);
		return billingInfoObj;
	}

	@Override
	public ApiResponse getMemberById(UUID id) {
		Member member = null;
		try {
			member = memberRepo.findByIdAndStatus(id);
			if (!ObjectUtils.isEmpty(member)) {
				MemberResponse memberResponse = new MemberResponse(member.getId(), member.getProfileUrl(),
						member.getMemberName(), member.getEmail(), member.getPhoneNumber(), member.getStatus(),
						member.getJoiningDate(), null);
				return new ApiResponse(HttpStatus.OK, memberResponse, false);
			}
			log.error("Not found error getting member by ID: {}", id);
			return new ApiResponse(HttpStatus.OK, String.format("Not found error getting member by ID: %s", id), false);
		} catch (Exception e) {
			log.error("Error fetching member by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting data", true);
	}

	@Override
	public ApiResponse updateMember(UUID id, MultipartFile profileImg, MemberRequest updatedMember) {
		Member member = null;
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var existingMember = memberRepo.findById(id);
			if (existingMember.isPresent()) {

				var name = updatedMember.getMemberName();
				var email = updatedMember.getEmail();
				var phNumber = updatedMember.getPhoneNumber();
				var status = updatedMember.getStatus();

				var extMemberObj = existingMember.get();
				if (profileImg != null && !profileImg.isEmpty()) {
					if (extMemberObj.getProfileUrl() != null) {
						s3ServiceConfig.deleteFile(extMemberObj.getProfileUrl());
					}
					String newProfileUrl = s3ServiceConfig.uploadFile(profileImg, id);
					extMemberObj.setProfileUrl(newProfileUrl);
				}
				if (ObjectUtils.isNotEmpty(name)) {
					extMemberObj.setMemberName(updatedMember.getMemberName());
				}
				if (ObjectUtils.isNotEmpty(email)) {
					extMemberObj.setEmail(updatedMember.getEmail());
				}
				if (ObjectUtils.isNotEmpty(phNumber)) {
					extMemberObj.setPhoneNumber(updatedMember.getPhoneNumber());
				}
				if (ObjectUtils.isNotEmpty(status)) {
					extMemberObj.setStatus(updatedMember.getStatus());
				}
				extMemberObj.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				memberRepo.save(extMemberObj);
				member = memberRepo.findByIdAndStatus(id);
				if (ObjectUtils.isNotEmpty(member)) {
					MemberResponse response = new MemberResponse(member.getId(), member.getProfileUrl(),
							member.getMemberName(), member.getEmail(), member.getPhoneNumber(), member.getStatus(),
							member.getJoiningDate(), null);
					return new ApiResponse(HttpStatus.OK, "Member details updated successfully", response, false);
				}
				// this statement execute after updating the member status
				return new ApiResponse(HttpStatus.OK, "Member details updated successfully", false);
			} else {
				log.error("Not found error getting member by ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Failed to update member by ID:{} ", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while updating data", true);
	}

	@Override
	public ApiResponse dltMemberById(UUID id) {
		try {
			if (ObjectUtils.isEmpty(id)) {
				throw new ParameterMissingException("Id is missing");
			}
			var memberPresentRes = memberRepo.findByIdAndStatus(id);
			if (!ObjectUtils.isEmpty(memberPresentRes)) {
//				if (memberPresentRes.get().getProfileUrl() != null) {
//					s3ServiceConfig.deleteFile(memberPresentRes.get().getProfileUrl());
//				}
				memberPresentRes.setStatus(false);
				memberPresentRes.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
				memberRepo.save(memberPresentRes);
				return new ApiResponse(HttpStatus.OK, "Member deleted successfully", false);
			}
			log.error("Not found error getting member by ID: {}", id);
		} catch (Exception e) {
			log.error("Error getting while deleting member by ID {}", id);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while deleting data", true);
	}

	@Override
	public ApiResponse getMemberList() {
		List<MemberResponse> memberResponses = new ArrayList<>();
		try {
			List<Member> memberListRes = memberRepo.findByStatusTrue();
			memberResponses = memberListRes.stream()
					.map(member -> new MemberResponse(member.getId(), member.getProfileUrl(), member.getMemberName(),
							member.getEmail(), member.getPhoneNumber(), member.getStatus(), member.getJoiningDate(),
							null))
					.collect(Collectors.toList());
			return new ApiResponse(HttpStatus.OK, memberResponses, false);
		} catch (Exception e) {
			log.error("Error while getting all the members");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting list of data", true);
	}

	@Override
	public ApiResponse getMemberCount() {
		Integer countResult = null;
		try {
			countResult = (int) memberRepo.count();
			return new ApiResponse(HttpStatus.OK, countResult, false);
		} catch (Exception e) {
			log.error("Error while getting member count");
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting member count", true);
	}

	@Override
	public ApiResponse getMemberBillingById(UUID id) {
		Member member = null;
		try {
			member = memberRepo.findByIdAndStatus(id);
			if (!ObjectUtils.isEmpty(member)) {
				BillingInfo biObj = memberRepo.findByMemberId(id);
				BillingResponse billingResponse = new BillingResponse(biObj.getBillingId(), biObj.getMemberId(),
						biObj.getJoiningDate(), biObj.getDueDate(), biObj.getCategory(), biObj.getPaidAmount(),
						biObj.getBalanceAmount(), biObj.getPaymentStatus());
				MemberResponse memberResponse = new MemberResponse(member.getId(), member.getProfileUrl(),
						member.getMemberName(), member.getEmail(), member.getPhoneNumber(), member.getStatus(),
						member.getJoiningDate(), billingResponse);
				return new ApiResponse(HttpStatus.OK, memberResponse, false);
			}
			log.error("Not found error getting member billing by ID: {}", id);
			return new ApiResponse(HttpStatus.OK, String.format("Not found error getting member billing by ID: %s", id),
					false);
		} catch (Exception e) {
			log.error("Error fetching member billing by ID: {}", e.getMessage(), e);
		}
		return new ApiResponse(HttpStatus.BAD_REQUEST, "Error while getting data", true);
	}

}
