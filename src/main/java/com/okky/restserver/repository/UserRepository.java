package com.okky.restserver.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.okky.restserver.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUuid(UUID uuid);
	Optional<User> findByUserId(String userId);
}
