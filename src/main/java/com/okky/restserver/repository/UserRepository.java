package com.okky.restserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okky.restserver.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByUserName(String userName);
}
