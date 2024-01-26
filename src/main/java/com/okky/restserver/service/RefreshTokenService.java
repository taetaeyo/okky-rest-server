//package com.okky.restserver.service;
//
//import org.springframework.stereotype.Service;
//
//import com.okky.restserver.domain.RefreshToken;
//import com.okky.restserver.repository.RefreshTokenRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class RefreshTokenService {
//	private final RefreshTokenRepository refreshTokenRepository;
//	
//	public RefreshToken findByRefreshToken(String refreshToken) {
//        return refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
//    }
//}
