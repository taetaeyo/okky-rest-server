package com.okky.restserver.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.okky.restserver.dto.ResponseCode;
import com.okky.restserver.security.SecurityConstants;
import com.okky.restserver.security.jwt.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 환경설정을 구성하는 단계에서 Filter로 등록한 클래스
 * 지정한 URL 별 JWT 유효성 검증을 수행하며 직접적인 사용자 '인증'을 확인
 * 
 * @author taekwon
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter{
	
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		
		// Header Check
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
		log.info("AuthorizationHeader Check {}", authorizationHeader);

		String token = getAccessToken(authorizationHeader);
		
		// 토큰 유효 check
		if (StringUtils.hasText(token) && jwtProvider.validationToken(token)) {
			// 인증 정보 설정
			Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {	// 토큰 유효하지 않음
			log.error("Token validation Fail");
			request.setAttribute("exception", ResponseCode.E0002.name());
		}
		
		filterChain.doFilter(request, response);
	}
    
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        }

        return null;
    }
}
