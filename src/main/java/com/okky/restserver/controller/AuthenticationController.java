package com.okky.restserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.service.UserService;

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
@Controller
public class AuthenticationController {
	
	private final UserService userService;

	@PostMapping(value = "/login")
	public String login(@RequestBody UserRequestDto request) {
		return "";
	}
}
