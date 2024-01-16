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

import com.okky.restserver.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

	private final JwtProperties jwtProperties;
	private Key key;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(Authentication authentication, Duration expired) {
		Date now = new Date();

		return createToken(new Date(now.getTime() + expired.toMillis()), authentication);
	}

	private String createToken(Date validity, Authentication authentication) {

		String authorities = authentication.getAuthorities().stream()
											.map(GrantedAuthority::getAuthority)
											.collect(Collectors.joining(","));

		return Jwts.builder()
		         .setSubject(authentication.getName())
		         .claim(SecurityConstants.AUTHORITIES_KEY, authorities)
		         .signWith(key, SignatureAlgorithm.HS512)
		         .setExpiration(validity)
		         .compact();
		
//		return Jwts.builder()
//				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//				.setIssuer(jwtProperties.getIssuer())
//				.setExpiration(validity).setSubject(signInDto.getId()).claim("id", authorities)
//				// 서명 : 비밀값과 함께 해시값을 HS256으로 암호화
//				.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()).compact();
	}

	// JWT token 유효성 검증 메서드
	public boolean validToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}

		return false;
	}

	// token 기반으로 인증 정보를 가져오는 메서드
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(SecurityConstants.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

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
		return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
	}

}
