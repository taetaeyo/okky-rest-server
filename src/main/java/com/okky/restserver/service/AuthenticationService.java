package com.okky.restserver.service;

import org.springframework.stereotype.Service;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.repository.UserRepository;
import com.okky.restserver.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	public UserResponseDto login(User user) {
//		return new UserResponseDto(user.getId(), jwtProvider.generateToken(user, null));
		
		return null;
	}
}
