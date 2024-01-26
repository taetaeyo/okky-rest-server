package com.okky.restserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
	
	private final UserService userService;
	
	@Transactional
	@PostMapping("/sign-up")
	public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto request) {
		return ResponseEntity.ok(userService.signUp(request));
	}
	
}
