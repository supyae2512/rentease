package com.rentease.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rentease.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	
	Optional<User> findByEmail(String email); // for login

    boolean existsByEmail(String email); // for registration validation

}
