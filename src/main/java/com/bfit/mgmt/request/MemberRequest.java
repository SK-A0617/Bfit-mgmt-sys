package com.bfit.mgmt.request;

import lombok.Getter;

@Getter
public class MemberRequest {

	public String memberName;
	public String email;
	public String phoneNumber;
	public Boolean status;
	public String category;
	public Integer paidAmount;
}
