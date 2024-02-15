package com.okky.restserver.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.domain.User;
import com.okky.restserver.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

	private final UserService userService;
	
	@Tag(name = "Test", description = "테스트")
	@Operation(summary = "Get test string value", description = "서버 접근 테스트를 위해 String을 반환한다. JWT 불필요")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "404", description = "실패") })
	@GetMapping("/test")
	public String test() {
//		String data = "okky project return test";
		String data = "jenkins test";
		
		log.info("Test Controller data {} ", data);
		
		return data;
	}

	@Tag(name = "Test", description = "테스트")
	@Operation(summary = "Get test user value", description = "유저 정보를 테스트한다. JWT 필요")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "404", description = "실패") })
	@GetMapping("/test/user")
	public List<User> getUsers() {
		log.info("TEST Users Controller data");
		
		return userService.getUsersList();
	}
	
}
