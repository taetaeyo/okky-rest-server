package com.okky.restserver.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "USER", description = "회원")
public class UserController {
	
	private final UserService userService;
	
	@Operation(summary = "회원 가입", description = "회원 가입 진행")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원 가입 성공", 
			content = @Content(examples = {@ExampleObject(
												name = "insertUser",
								        		summary = "회원 가입 성공 예시",
								        		description = "회원 가입 성공 예시",
								        		value = "{\"status\":200,\"code\":\"S0000\",\"message\":\"Success\",\"result\":{\"uuid\":\"74ba9f95-4460-47e1-a7bb-ec6f8b4a84de\",\"userId\":\"test01\",\"email\":\"aaa@naver.com\",\"userName\":\"taetae\",\"nickName\":\"testNickName\"}}"
							        		)},
								mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@PostMapping("/sign-up")
	public UserResponseDto signUp(@RequestBody 
				@io.swagger.v3.oas.annotations.parameters.RequestBody UserRequestDto request) {
		return userService.signUp(request);
	}
	
	@Operation(summary = "회원 정보 조회", description = "UUID를 이용하여 회원 정보 조회")
	@GetMapping("/user/{uuid}")
	public UserResponseDto getUserInfo(@PathVariable("uuid") final UUID uuid) {
		return userService.findByUuid(uuid);
	}
	
}
