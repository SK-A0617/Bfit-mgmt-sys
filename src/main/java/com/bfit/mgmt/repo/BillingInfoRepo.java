package com.bfit.mgmt.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.BillingInfo;

@Repository
public interface BillingInfoRepo extends JpaRepository<BillingInfo, UUID>{

}
