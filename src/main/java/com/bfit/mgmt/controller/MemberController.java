package com.bfit.mgmt.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfit.mgmt.entity.Member;
import com.bfit.mgmt.service.MemberService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class MemberController {

	@Autowired
	private MemberService memberService;

	@PostMapping("/saveMember")
	public ApiResponse<Member> saveMember(@RequestBody Member member) {
		var data = memberService.saveMember(member);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@GetMapping("/getMemberById")
	public ApiResponse<Member> getMemberById(@RequestParam UUID id) {
		var data = memberService.getMemberById(id);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@GetMapping("/getAllMemberList")
	public ApiResponse<List<Member>> getMemberList() {
		var data = memberService.getMemberList();
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@PutMapping("/updateMember/{id}")
	public ApiResponse<Member> updateMember(@PathVariable UUID id, @RequestBody Member member) {
		var data = memberService.updateMember(id, member);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	@DeleteMapping("/deleteMemeber/{id}")
	public ApiResponse<String> dltMemberById(@PathVariable UUID id) {
		var data = memberService.dltMemberById(id);
		return new ApiResponse<>(HttpStatus.OK, data);
	}

}
