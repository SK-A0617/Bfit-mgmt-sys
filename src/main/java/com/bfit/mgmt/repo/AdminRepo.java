package com.bfit.mgmt.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID>{

}
