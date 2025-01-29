package com.bfit.mgmt.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.Coach;

@Repository
public interface CoachRepo extends JpaRepository<Coach, UUID> {
	
	@Query("SELECT c FROM Coach c WHERE c.id = :id AND c.status = true")
	Coach findByIdAndStatus(@Param("id") UUID id);

	List<Coach> findByStatusTrue();

}
