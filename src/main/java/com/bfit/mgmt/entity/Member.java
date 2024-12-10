package com.bfit.mgmt.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "profile")
	private String profile;

	@Column(name = "memeber_name", nullable = false)
	private String memeberName;

	@Email
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "status", nullable = false)
	private Boolean status;

	@Column(name = "joining_date", nullable = false)
	private LocalDate joiningDate;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Timestamp updatedAt;
	

	public Member(String profile, String memeberName, String email, String phoneNumber, Boolean status,
			LocalDate joiningDate) {
		super();
		this.profile = profile;
		this.memeberName = memeberName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.joiningDate = joiningDate;
	}

}
