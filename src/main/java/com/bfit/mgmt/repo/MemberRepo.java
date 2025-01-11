package com.bfit.mgmt.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.Member;

@Repository
public interface MemberRepo extends JpaRepository<Member, UUID>{

	List<Member> findByStatusTrue();

}
