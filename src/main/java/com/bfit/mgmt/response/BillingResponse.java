package com.bfit.mgmt.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingResponse {
	
	public UUID billingId;
	public UUID memberId;
	public LocalDate joiningDate;
	public LocalDate dueDate;
	public String category;
	public Integer paidAmount;
	public Integer balanceAmount;
	public String paymentStatus;

}
