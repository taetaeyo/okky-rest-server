package com.okky.restserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.okky.restserver.dto.BannerDto;
import com.okky.restserver.repository.BannerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {
	
	private final BannerRepository bannerRepository;

	public List<BannerDto> getBannerList() {
		// Entity -> Dto
		return bannerRepository.findAll().stream()
								.map(BannerDto::convertToDto)
								.collect(Collectors.toList());
	}
}
