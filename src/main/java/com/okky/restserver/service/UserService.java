package com.okky.restserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.okky.restserver.domain.User;
import com.okky.restserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	public List<User> getUsersList(){
    	return userRepository.findAll();
    }

	public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}