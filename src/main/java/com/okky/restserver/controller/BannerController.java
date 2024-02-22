package com.okky.restserver.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.BannerDto;
import com.okky.restserver.service.BannerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Banner", description = "배너")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BannerController {
	
	private final BannerService bannerService;
	
	@Operation(summary = "배너 목록 조회", description = "1) HEADER에 x-api-key 값 필요\n "
														+ "2) 그 외 Request Parameter 불필요")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "배너 목록 조회 성공", content = @Content(examples = {
					@ExampleObject(name = "getBannerList", 
							summary = "배너 목록 조회 성공", 
							description = "배너 목록 조회 성공", 
							value = "{\"status\":200,\"code\":\"S0000\",\"message\":\"Success\",\"result\":{\"data\":[{\"name\":\"메인 center 배너\",\"type\":\".png\",\"url\":\"https://taetae.site/images/main_banner_01.png\",\"dateCreated\":\"2024-02-22 04:47:46\"}],\"type\":\"array\"}}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "400", description = "실패", content = @Content(examples = {
					@ExampleObject(name = "getBannerList", 
							summary = "실패", 
							description = "x-api-key 확인 필요 (400)", 
							value = "{\"status\":400,\"code\":\"E0000\",\"message\":\"The input value is invalied.\"}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "403", description = "실패", content = @Content(examples = {
					@ExampleObject(name = "getBannerList", 
							summary = "실패", 
							description = "x-api-key 확인 필요 (403)", 
							value = "{\"status\":400,\"code\":\"E0000\",\"message\":\"The input value is invalied.\"}") }, 
			mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "409", description = "배너 조회 실패", content = @Content(examples = {
					@ExampleObject(name = "getBannerList", 
							summary = "배너 조회 실패", 
							description = "배너 조회 실패 (409)", 
							value = "{\"status\":409,\"code\":\"E0006\",\"message\":\"This user is already registered.\"}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@GetMapping("/api/all/banner")
	public Object getBannerList() {
		Map<String, Object> result = new HashMap<>();
		List<BannerDto> data = bannerService.getBannerList();

		log.info("Get Banner List {}", data);
		
		result.put("type", "array");
		result.put("data", data);
		
		return result;
	}

}
