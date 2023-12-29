package com.okky.restserver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.domain.User;
import com.okky.restserver.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	
	private final UserService userService;
	
	@GetMapping("/test")
	@Operation(summary = "Get test string value", description = "서버 접근 테스트를 위해 String을 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
							@ApiResponse(responseCode = "404", description = "실패") })
	public String test() {
		String data = "okky project return test";
		
		LOGGER.info("Test Controller data :: " + data);
		
		return data;
	}
	
	
	@GetMapping("/testuser")
	@Operation(summary = "Get test user value", description = "유저 정보를 테스트한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
							@ApiResponse(responseCode = "404", description = "실패") })
	public List<User> getUsers() {
		LOGGER.info("TEST Users Controller data");
		
		return userService.getUsersList();
	}
}
