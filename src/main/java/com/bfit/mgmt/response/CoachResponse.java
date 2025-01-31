package com.bfit.mgmt.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoachResponse {

	public UUID id;
	public String profileUrl;
	public String coachName;
	public String email;
	public String phoneNumber;
	public Boolean status;
	public LocalDate joiningDate;
	
}

	
