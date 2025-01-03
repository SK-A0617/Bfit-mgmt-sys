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

import com.bfit.mgmt.request.CoachRequest;
import com.bfit.mgmt.service.CoachService;
import com.bfit.mgmt.util.ApiResponse;
import com.bfit.mgmt.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = Constants.REQPATH)
public class CoachController {

	@Autowired
	private CoachService coachService;

	@PostMapping("/createCoach")
	@Operation
	public ResponseEntity<ApiResponse> createCoach(
			@RequestPart(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestPart("coach") String coachJson) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		var coachRequest = objectMapper.readValue(coachJson, CoachRequest.class);
		var data = coachService.createCoach(profileImg, coachRequest);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getCoachById")
	public ResponseEntity<ApiResponse> getCoachById(@RequestParam UUID id) {
		var data = coachService.getCoachById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/getAllCoachList")
	public ResponseEntity<ApiResponse> getCoachList() {
		var data = coachService.getCoachList();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@PutMapping("/updateCoach/{id}")
	public ResponseEntity<ApiResponse> updateCoach(@PathVariable UUID id,
			@RequestPart("profileImg") MultipartFile profileImg, @RequestPart("coach") String coachJson)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		var coachRequest = objectMapper.readValue(coachJson, CoachRequest.class);
		var data = coachService.updateCoach(id, profileImg, coachRequest);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@DeleteMapping("/deleteCoach/{id}")
	public ResponseEntity<ApiResponse> dltCoachById(@PathVariable UUID id) {
		var data = coachService.dltCoachById(id);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}
