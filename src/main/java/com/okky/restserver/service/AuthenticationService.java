package com.okky.restserver.service;

import java.time.Duration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.okky.restserver.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	public String createNewAccessToken(String refreshToken) {
        // 1. refresh token 유효성 검사
        if(jwtProvider.validationToken(refreshToken)) {
        	// 2. DB refresh_token과 전달받은 refreshToken 일치여부 확인
            Long userSeq = refreshTokenService.findByRefreshToken(refreshToken).getUserSeq();
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            return jwtProvider.generateJwt(authentication, Duration.ofMinutes(10));
        } else {
        	throw new IllegalArgumentException("The refresh token is invalid.");
        }
    }
}
