package com.okky.restserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class TestController {

	@GetMapping("/test")
	@Operation(summary = "Get test string value", description = "서버 접근 테스트를 위해 String을 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
							@ApiResponse(responseCode = "404", description = "실패") })
	public String test() {
		String data = "okky project return test";
		System.out.println("TestController test");
		
		return data;
	}
}
