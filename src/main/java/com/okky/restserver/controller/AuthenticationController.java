package com.okky.restserver.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.JwtDto;
import com.okky.restserver.dto.SignInDto;
import com.okky.restserver.security.SecurityConstants;
import com.okky.restserver.security.jwt.JwtProvider;
import com.okky.restserver.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication
 * @author taekwon
 *
 */

@Slf4j
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {
	
	private final JwtProvider jwtProvider;
//	private final AuthenticationService authenticationService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@PostMapping(value = "/sign-in")
	public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto signInDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInDto.getId(), signInDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
//		String jwt = authenticationService.signIn(signInDto);
        String jwt = jwtProvider.generateToken(authentication, Duration.ofMinutes(30));
		
		return ResponseEntity.status(HttpStatus.OK)
								.body(new JwtDto(SecurityConstants.TOKEN_PREFIX, jwt));
	}
	
//	@PostMapping("/refresh-token")
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
//        String newAccessToken = authenticationService.createNewAccessToken(request.getRefreshToken());
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                				.body(new CreateAccessTokenResponse(newAccessToken));
//    }
}
