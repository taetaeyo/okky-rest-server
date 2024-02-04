package com.okky.restserver.security.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.okky.restserver.domain.RefreshToken;
import com.okky.restserver.repository.RefreshTokenRepository;
import com.okky.restserver.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider implements InitializingBean {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProperties jwtProperties;
	private Key key;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateJwt(Authentication authentication, Duration expired) {
		Date now = new Date();

		return createJwt(new Date(now.getTime() + expired.toMillis()), authentication);
	}
	
	public String generateRefreshToken(Authentication authentication, Duration expired) {
		Date now = new Date();
		Long userId = Long.parseLong(authentication.getName());
		String refreshToken = createRefreshToken(new Date(now.getTime() + expired.toMillis())
													, authentication);
		
		RefreshToken refreshTokenDomain = new RefreshToken(userId, refreshToken);
		refreshTokenRepository.save(refreshTokenDomain);
		
		return refreshToken;
	}

	// JWT 생성
	private String createJwt(Date validity, Authentication authentication) {

		String authorities = authentication.getAuthorities().stream()
											.map(GrantedAuthority::getAuthority)
											.collect(Collectors.joining(","));

		return Jwts.builder()
					.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
					.setSubject(authentication.getName())
					.claim(SecurityConstants.AUTHORITIES_KEY, authorities)
					.signWith(key, SignatureAlgorithm.HS512)
					.setExpiration(validity)
					.compact();
	}
	
	// Refresh Token 생성
	private String createRefreshToken(Date validity, Authentication authentication) {

		String authorities = authentication.getAuthorities().stream()
											.map(GrantedAuthority::getAuthority)
											.collect(Collectors.joining(","));

		return Jwts.builder()
					.setSubject(authentication.getName())
					.claim(SecurityConstants.AUTHORITIES_KEY, authorities)
					.signWith(key, SignatureAlgorithm.HS512)
					.setExpiration(validity)
					.compact();
	}

	// JWT token 유효성 검증 메서드
	public boolean validationToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다. {}", e);
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다. {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다. {}", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다. {}", e);
		}

		return false;
	}

	// token 기반으로 인증 정보를 가져오는 메서드
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);

		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(claims.get(SecurityConstants.AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	// token 기반으로 유저 ID를 가져오는 메서드
	public String getUserId(String token) {
		Claims claims = getClaims(token);
		return claims.get("id", String.class);
	}

	// token 기반으로 클레임 조회
	private Claims getClaims(String token) {
		return Jwts
	              .parserBuilder()
	              .setSigningKey(key)
	              .build()
	              .parseClaimsJws(token)
	              .getBody();
	}

}
