package com.okky.restserver.service;

import org.springframework.stereotype.Service;

import com.okky.restserver.repository.UserRepository;
import com.okky.restserver.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

}
