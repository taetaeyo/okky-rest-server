package com.okky.restserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.okky.restserver.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
