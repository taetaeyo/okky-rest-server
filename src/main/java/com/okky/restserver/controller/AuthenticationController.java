package com.okky.restserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Controller
public class AuthenticationController {

	@GetMapping(value = "/login")
	public String login() {
		return "";
	}
}
