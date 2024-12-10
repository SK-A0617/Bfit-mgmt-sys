package com.bfit.mgmt.repo;

import java.lang.reflect.Member;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<Member, UUID>{

}
