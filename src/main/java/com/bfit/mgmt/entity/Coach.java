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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "coach")
public class Coach {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "profile_url")
	private String profileUrl;

	@Transient
	private MultipartFile profileImg;

	@Column(name = "coach_name", nullable = false)
	private String coachName;

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

	public Coach(String profileUrl, String coachName, String email, String phoneNumber, Boolean status,
			LocalDate joiningDate, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.profileUrl = profileUrl;
		this.coachName = coachName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.joiningDate = joiningDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

}
