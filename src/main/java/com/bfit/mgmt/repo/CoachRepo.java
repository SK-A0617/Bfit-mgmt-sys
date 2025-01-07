package com.bfit.mgmt.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.Coach;

@Repository
public interface CoachRepo extends JpaRepository<Coach, UUID> {

}