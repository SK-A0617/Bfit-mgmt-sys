package com.bfit.mgmt.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "billing_info")
public class BillingInfo {

	@Id
	@Column(name = "billing_id", updatable = false, nullable = false)
	private UUID billingId;

	@Column(name = "member_id", updatable = false, nullable = false)
	private UUID memberId;

	@Column(name = "joining_date", nullable = false)
	private LocalDate joiningDate;

	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;

	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "category_amount", nullable = false)
	private Integer categoryAmount;

	@Column(name = "paid_amount", nullable = false)
	private Integer paidAmount;

	@Column(name = "balance_amount", nullable = false)
	private Integer balanceAmount;

	@Column(name = "payment_status", nullable = false)
	private String paymentStatus;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Timestamp updatedAt;

	public BillingInfo(UUID billingId, UUID memberId, LocalDate joiningDate, LocalDate dueDate, String category,
			Integer categoryAmount, Integer paidAmount, Integer balanceAmount, String paymentStatus,
			Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.billingId = billingId;
		this.memberId = memberId;
		this.joiningDate = joiningDate;
		this.dueDate = dueDate;
		this.category = category;
		this.categoryAmount = categoryAmount;
		this.paidAmount = paidAmount;
		this.balanceAmount = balanceAmount;
		this.paymentStatus = paymentStatus;
		this.createdAt = createdAt;
	}

}
