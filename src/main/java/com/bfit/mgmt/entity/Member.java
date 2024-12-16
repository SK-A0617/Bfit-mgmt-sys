package com.bfit.mgmt.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "profile_url")
	private String profileUrl;
	
	@Transient
	private MultipartFile profileImg;

	@Column(name = "member_name", nullable = false)
	private String memberName;

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

	public Member(String profileUrl, String memberName, @Email String email,
			String phoneNumber, Boolean status, LocalDate joiningDate, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.profileUrl = profileUrl;
		this.memberName = memberName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.joiningDate = joiningDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	
	
}
