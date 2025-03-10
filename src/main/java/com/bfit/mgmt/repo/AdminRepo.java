package com.bfit.mgmt.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bfit.mgmt.entity.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID>{
	
	@Query("SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password")
	Admin getByEmail(@Param("email")String email, @Param("password")String password);

}
