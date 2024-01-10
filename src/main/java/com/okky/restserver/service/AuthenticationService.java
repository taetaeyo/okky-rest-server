package com.okky.restserver.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.repository.UserRepository;
import com.okky.restserver.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	public String login(User user) {
		String temp = jwtProvider.generateToken(user, Duration.ofMinutes(10));
//		return new UserResponseDto(user.getId(), jwtProvider.generateToken(user, Duration.ofMinutes(10)));
		
		return "servicetest";
	}
}
