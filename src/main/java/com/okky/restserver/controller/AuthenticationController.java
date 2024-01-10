package com.okky.restserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication
 * @author taekwon
 *
 */

@Slf4j
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;

	@PostMapping(value = "/login")
	public UserResponseDto login(@RequestBody User request) {
		
		authenticationService.login(request);
		return null;
	}
}
