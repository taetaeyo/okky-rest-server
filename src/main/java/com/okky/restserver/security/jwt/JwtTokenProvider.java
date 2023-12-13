package com.okky.restserver.security.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.okky.restserver.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	public String generateToken(User user, Duration expiredAt) {
		Date now = new Date();
		
		return createToken(new Date(now.getTime() + expiredAt.toMillis()), user);
	}
	
	private String createToken(Date expiry, User user) {
		Date now = new Date();
		
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuedAt(now)
				.setExpiration(now)
				.setSubject(user.getEmail())
				.claim("id", user.getId())
				.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
				.compact();
	}
	
	// JWT token 유효성 검증 메서드
	public boolean validToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(jwtProperties.getSecretKey())	// 복호화
					.parseClaimsJws(token);
			
			return true;
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return false;
		}
	}
	
	// token 기반으로 인증 정보를 가져오는 메서드
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

		return new UsernamePasswordAuthenticationToken(
				new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), 
				token,
				authorities);
	}
	
	// token 기반으로 유저 ID를 가져오는 메서드
	public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

	// token 기반으로 클레임 조회
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
    
}
