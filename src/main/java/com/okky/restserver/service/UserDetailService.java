package com.okky.restserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.okky.restserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService{

	private final UserRepository userRepository;
	
	// 사용자 이름으로 사용자의 정보를 가져오는 메서드
	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException(username));
	}

}
