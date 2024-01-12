package com.okky.restserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.CreateAccessTokenRequest;
import com.okky.restserver.dto.CreateAccessTokenResponse;
import com.okky.restserver.dto.JwtDto;
import com.okky.restserver.dto.SignInDto;
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
	
	private final AuthenticationService authenticationService;
	
	@PostMapping(value = "/sign-in")
	public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto signInDto) {
		String jwt = authenticationService.signIn(signInDto);
		
		return ResponseEntity.status(HttpStatus.CREATED)
								.body(new JwtDto(jwt));
	}
	
//	@PostMapping("/refresh-token")
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
//        String newAccessToken = authenticationService.createNewAccessToken(request.getRefreshToken());
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                				.body(new CreateAccessTokenResponse(newAccessToken));
//    }
}
