package com.bfit.mgmt.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bfit.mgmt.request.MemberRequest;
import com.bfit.mgmt.service.MemberService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class MemberController {

	@Autowired
	private MemberService memberService;

	@PostMapping("/createMember")
	@Operation
	public ResponseEntity<ApiResponse> createMember(
			@RequestPart(name = "profileImg", required = false)MultipartFile profileImg,
			@RequestPart("member") String memberJson, String category, String paymentStatus) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		var memberRequest = objectMapper.readValue(memberJson, MemberRequest.class);
		var data = memberService.createMember(profileImg, memberRequest,category,paymentStatus);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getMemberById")
	public ResponseEntity<ApiResponse> getMemberById(@RequestParam UUID id) {
		var data = memberService.getMemberById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getAllMemberList")
	public ResponseEntity<ApiResponse> getMemberList() {
		var data = memberService.getMemberList();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PutMapping("/updateMember/{id}")
	public ResponseEntity<ApiResponse> updateMember(@PathVariable UUID id,
			@RequestPart(name = "profileImg", required = false) MultipartFile profileImg, @RequestPart("member") String memberJson)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		var memberRequest = objectMapper.readValue(memberJson, MemberRequest.class);
		var data = memberService.updateMember(id, profileImg, memberRequest);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping("/deleteMemeber/{id}")
	public ResponseEntity<ApiResponse> dltMemberById(@PathVariable UUID id) {
		var data = memberService.dltMemberById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@GetMapping("/memberCount")
	public ResponseEntity<ApiResponse> countOfMember(){
		var data = memberService.getMemberCount();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}
