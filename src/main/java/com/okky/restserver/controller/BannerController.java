package com.okky.restserver.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.BannerDto;
import com.okky.restserver.service.BannerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BannerController {
	
	private final BannerService bannerService;
	
	@Operation(summary = "배너", description = "배너 목록 조회")
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
