package com.bfit.mgmt.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.BillingInfo;
import com.bfit.mgmt.entity.Member;

@Repository
public interface MemberRepo extends JpaRepository<Member, UUID>{
	
	@Query("SELECT m FROM Member m WHERE m.id = :id AND m.status = true")
	Member findByIdAndStatus(@Param("id") UUID id);
	
	@Query("SELECT b FROM BillingInfo b WHERE b.memberId = :id")
	BillingInfo findByMemberId(@Param("id")UUID id);

	List<Member> findByStatusTrue();

}
