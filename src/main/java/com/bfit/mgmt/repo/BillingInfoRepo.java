package com.bfit.mgmt.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.BillingInfo;

@Repository
public interface BillingInfoRepo extends JpaRepository<BillingInfo, UUID>{
	
	@Query("SELECT b FROM BillingInfo b WHERE b.dueDate = :dueDate")
	List<BillingInfo> findByDueDate(LocalDate dueDate);

}
